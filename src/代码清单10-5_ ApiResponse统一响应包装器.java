package com.xrtech.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统一API响应包装器
 * 所有REST API响应都使用此结构
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String version;
    private Long timestamp;
    private ErrorDetails error;

    private ApiResponse() {}

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = "OK";
        response.data = data;
        response.timestamp = System.currentTimeMillis();
        return response;
    }

    public static <T> ApiResponse<T> success(T data, String version) {
        ApiResponse<T> response = success(data);
        response.version = version;
        return response;
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        response.error = new ErrorDetails(code, message);
        response.timestamp = System.currentTimeMillis();
        return response;
    }

    public record ErrorDetails(String code, String message) {}
}