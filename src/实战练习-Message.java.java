package com.xrtech.websocket.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private String sender;
    @JsonProperty("recipient")
    private String recipient;
    private String content;
    private String type = "chat";
    private long timestamp;

    public Message() {
        this.timestamp = System.currentTimeMillis();
    }

    // Getter和Setter（省略，可使用Lombok @Data）

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}