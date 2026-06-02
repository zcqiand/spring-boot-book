package com.xrtech.websocket.controller;

import com.xrtech.websocket.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 处理私信
     * 路径：/app/chat.private
     *
     * @param message 包含recipient（收件人）字段的消息对象
     *
     * 消息路由：
     * 1. 客户端发送消息到 /app/chat.private
     * 2. @MessageMapping解析Message对象
     * 3. convertAndSendToUser将消息发送到 /user/{recipient}/queue/messages
     * 4. 只有recipient指定的用户能收到（通过订阅/user/queue/messages）
     *
     * 注意：
     * - 消息只会推送给指定用户，其他用户无法看到
     * - 即使目标用户不在线，消息也会被STOMP代理缓存
     */
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(Message message) {
        System.out.println("[私信] " + message.getSender() + " -> " +
                          message.getRecipient() + "：" + message.getContent());

        // 发送点对点消息
        // 格式：/user/{username}/queue/{queuename}
        messagingTemplate.convertAndSendToUser(
            message.getRecipient(),   // 收件人用户名
            "/queue/messages",        // 队列名称
            message                   // 消息体
        );
    }
}