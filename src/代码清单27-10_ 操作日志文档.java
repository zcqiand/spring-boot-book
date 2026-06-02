package com.example.dal.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "t_operation_log")
public class OperationLog {

    @Id
    private String id;

    @Field("user_id")
    private Long userId;

    @Field("username")
    private String username;

    @Field("operation_type")
    private String operationType;

    @Field("operation_desc")
    private String operationDesc;

    @Field("target_type")
    private String targetType;

    @Field("target_id")
    private String targetId;

    @Field("request_params")
    private Map<String, Object> requestParams;

    @Field("response_result")
    private Map<String, Object> responseResult;

    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    @Field("created_at")
    private LocalDateTime createdAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getOperationDesc() { return operationDesc; }
    public void setOperationDesc(String operationDesc) { this.operationDesc = operationDesc; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    public Map<String, Object> getRequestParams() { return requestParams; }
    public void setRequestParams(Map<String, Object> requestParams) { this.requestParams = requestParams; }
    public Map<String, Object> getResponseResult() { return responseResult; }
    public void setResponseResult(Map<String, Object> responseResult) { this.responseResult = responseResult; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}