package com.xrtech.websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    /**
     * 处理广播消息
     * 路径：/app/notification（由setApplicationDestinationPrefixes添加/app前缀）
     *
     * @param message 客户端发送的消息内容
     * @return 消息会被发送到 @SendTo 指定的 /topic/notifications
     *
     * 注意：
     * - 所有订阅了 /topic/notifications 的客户端都会收到
     * - 适合系统公告、全局通知等场景
     */
    @MessageMapping("/notification")
    @SendTo("/topic/notifications")
    public String broadcast(String message) {
        System.out.println("[广播] 收到消息：" + message);
        return message;
    }
}