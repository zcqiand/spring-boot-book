package com.xrtech.api.response;

import java.time.Instant;
import java.util.Map;

/**
 * 统一 API 响应结构
 * 所有 Controller 返回均使用此结构，保证前端解析一致性
 */
public record ApiResponse<T>(
    boolean success,
    int code,
    String message,
    T data,
    Map<String, String> errors,
    Instant timestamp,
    String path
) {

    /**
     * 快速创建成功响应
     */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, 0, "操作成功", data, null, Instant.now(), null);
    }

    /**
     * 快速创建成功响应（带消息）
     */
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, 0, message, data, null, Instant.now(), null);
    }

    /**
     * 创建失败响应
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(false, code, message, null, null, Instant.now(), null);
    }

    /**
     * 创建失败响应（带错误明细）
     */
    public static <T> ApiResponse<T> fail(int code, String message, Map<String, String> errors) {
        return new ApiResponse<>(false, code, message, null, errors, Instant.now(), null);
    }

    /**
     * 带请求路径的失败响应（异常处理时使用）
     */
    public static <T> ApiResponse<T> fail(int code, String message, String path) {
        return new ApiResponse<>(false, code, message, null, null, Instant.now(), path);
    }
}