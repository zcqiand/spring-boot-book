/**
 * WebSocket客户端连接管理器
 *
 * 功能说明：
 * - 管理STOMP over WebSocket/SockJS的连接生命周期
 * - 提供消息订阅和发送的统一接口
 *
 * 使用示例：
 *   const client = new WebSocketClient('http://localhost:8080/ws');
 *   client.connect('username', 'password', onConnect, onError);
 */

class WebSocketClient {
    constructor(wsUrl) {
        // WebSocket服务端地址（带SockJS后缀）
        // 注意：/ws是WebSocketConfig中注册的端点路径
        this.wsUrl = wsUrl;
        // STOMP客户端实例
        this.client = null;
        // 当前登录用户
        this.currentUser = null;
        // 连接状态
        this.connected = false;
        // 订阅记录（用于取消订阅）
        this.subscriptions = new Map();
    }

    /**
     * 建立WebSocket连接
     *
     * @param {string} username - 用户名（作为simpUser用于点对点消息）
     * @param {string} password - 密码（STOMP CONNECT帧的认证信息）
     * @param {function} onConnect - 连接成功回调
     * @param {function} onError - 连接失败回调
     *
     * 注意：
     * - Spring Security的STOMP认证需要配置spring-messaging
     * - 若无Security配置，username/password可随意填写
     */
    connect(username, password, onConnect, onError) {
        // 创建SockJS连接（自动处理WebSocket降级）
        // 若浏览器完全不支持SockJS/WS，会降级为轮询
        const socket = new SockJS(this.wsUrl);

        // 创建STOMP客户端（over SockJS）
        this.client = Stomp.over(socket);

        // 关闭STOMP默认日志输出（生产环境建议关闭）
        this.client.debug = function(str) {
            // console.log(str);  // 调试时取消注释
        };

        // STOMP连接参数（对应Spring Security的SimpUser）
        const headers = {
            'username': username,
            'password': password
        };

        // 建立连接
        this.client.connect(
            headers,
            // 连接成功回调
            (frame) => {
                this.connected = true;
                this.currentUser = username;
                console.log('[STOMP] 连接成功，用户：' + username);
                if (onConnect) onConnect(frame);
            },
            // 连接失败/断开回调
            (error) => {
                this.connected = false;
                console.error('[STOMP] 连接断开：', error);
                if (onError) onError(error);
            }
        );
    }

    /**
     * 订阅广播主题（所有用户都能收到）
     *
     * @param {string} destination - 订阅路径（如 /topic/notifications）
     * @param {function} callback - 消息回调函数，参数为消息体
     *
     * 示例：
     *   client.subscribeBroadcast('/topic/notifications', (message) => {
     *       console.log('收到广播：', message.body);
     *   });
     *
     * 注意：
     * - /topic前缀表示广播消息，由simpleBroker处理
     * - 订阅后，所有发送到这个destination的消息都会触发callback
     */
    subscribeBroadcast(destination, callback) {
        if (!this.connected) {
            console.error('[STOMP] 未连接，无法订阅');
            return null;
        }

        // 发起SUBSCRIBE帧
        const subscription = this.client.subscribe(destination, (message) => {
            // message.body 是STOMP帧的BODY部分（通常为JSON字符串）
            // 若消息为JSON，先解析
            try {
                const body = JSON.parse(message.body);
                callback(body, message);
            } catch (e) {
                // 非JSON格式，直接返回原始body
                callback(message.body, message);
            }
        });

        this.subscriptions.set(destination, subscription);
        console.log('[STOMP] 已订阅广播主题：' + destination);
        return subscription;
    }

    /**
     * 订阅点对点消息（只有指定用户能收到）
     *
     * @param {string} destination - 订阅路径（格式：/user/{username}/queue/{queue-name}）
     * @param {function} callback - 消息回调函数
     *
     * 示例：
     *   client.subscribePrivate('/user/queue/messages', (message) => {
     *       console.log('收到私信：', message);
     *   });
     *
     * 注意：
     * - /user/{username} 是Spring STOMP的自动路由前缀
     * - 服务端使用convertAndSendToUser时自动使用这个前缀
     * - 用户名在connect时通过header传入的username确定
     */
    subscribePrivate(destination, callback) {
        if (!this.connected) {
            console.error('[STOMP] 未连接，无法订阅');
            return null;
        }

        const subscription = this.client.subscribe(destination, (message) => {
            try {
                const body = JSON.parse(message.body);
                callback(body, message);
            } catch (e) {
                callback(message.body, message);
            }
        });

        this.subscriptions.set(destination, subscription);
        console.log('[STOMP] 已订阅私有队列：' + destination);
        return subscription;
    }

    /**
     * 发送广播消息（@MessageMapping路由到@SendTo）
     *
     * @param {string} destination - 目标路径（如 /app/notification）
     * @param {object|string} body - 消息内容
     *
     * 示例：
     *   client.sendBroadcast('/app/notification', 'Hello World');
     *   client.sendBroadcast('/app/notification', { type: 'alert', msg: 'warning' });
     *
     * 注意：
     * - /app前缀是configureMessageBroker中设置的ApplicationDestinationPrefixes
     * - @MessageMapping("/notification") 监听 /app/notification
     */
    sendBroadcast(destination, body) {
        if (!this.connected) {
            console.error('[STOMP] 未连接，无法发送');
            return;
        }

        // SEND帧的目标路径不需要/app前缀，会自动添加
        // 或者直接使用完整路径
        const fullDestination = destination.startsWith('/app')
            ? destination
            : '/app' + destination;

        this.client.send(
            fullDestination,
            {},
            typeof body === 'string' ? body : JSON.stringify(body)
        );

        console.log('[STOMP] 已发送广播：' + fullDestination, body);
    }

    /**
     * 发送点对点消息
     *
     * @param {string} destination - 目标路径（如 /app/chat.private）
     * @param {object} message - 消息对象，需包含recipient字段
     *
     * 示例：
     *   client.sendPrivate('/app/chat.private', {
     *       recipient: 'alice',
     *       content: 'Hello Alice!',
     *       sender: 'bob'
     *   });
     *
     * 注意：
     * - recipient字段用于服务端路由到指定用户
     * - 服务端ChatController使用convertAndSendToUser发送
     */
    sendPrivate(destination, message) {
        if (!this.connected) {
            console.error('[STOMP] 未连接，无法发送');
            return;
        }

        const fullDestination = destination.startsWith('/app')
            ? destination
            : '/app' + destination;

        this.client.send(
            fullDestination,
            {},
            JSON.stringify(message)
        );

        console.log('[STOMP] 已发送私信给 ' + message.recipient, message);
    }

    /**
     * 断开连接
     */
    disconnect() {
        if (this.client) {
            // 取消所有订阅
            this.subscriptions.forEach((sub) => {
                sub.unsubscribe();
            });
            this.subscriptions.clear();

            // 断开连接
            this.client.disconnect();
            this.connected = false;
            this.currentUser = null;
            console.log('[STOMP] 已断开连接');
        }
    }
}