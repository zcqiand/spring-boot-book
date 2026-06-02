package com.xrtech.websocket.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WebSocket消息实体类
 *
 * 功能说明：
 * - 用于点对点消息传输和广播消息的结构化载体
 * - 包含发送者、接收者、消息内容和时间戳等核心字段
 */
public class Message {

    // 发送者用户名（可选，用于群发时标识来源）
    private String sender;

    // 接收者用户名（点对点消息必填，广播消息可为空）
    @JsonProperty("recipient")
    private String recipient;

    // 消息内容（支持纯文本或JSON字符串）
    private String content;

    // 消息类型：chat-聊天消息，system-系统通知，notification-公告
    private String type = "chat";

    // 消息发送时间戳（毫秒）
    private long timestamp;

    // 默认构造函数（Jackson反序列化需要）
    public Message() {
        this.timestamp = System.currentTimeMillis();
    }

    // 全量构造函数
    public Message(String sender, String recipient, String content, String type) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    // --- Getter和Setter方法 ---

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 判断是否为点对点消息
     * 注意：recipient不为空且不为broadcast时为点对点消息
     */
    public boolean isPrivate() {
        return recipient != null && !recipient.isEmpty() && !"broadcast".equals(recipient);
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}