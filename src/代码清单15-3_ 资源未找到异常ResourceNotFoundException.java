package com.xrtech.api.exception;

import org.springframework.http.HttpStatus;

/**
 * 资源未找到异常
 * 当请求的资源（用户、订单、文章等）不存在时抛出
 */
public class ResourceNotFoundException<T> extends BusinessException<T> {

    public ResourceNotFoundException(T resourceType, Object identifier) {
        super(resourceType, buildMessage(resourceType, identifier), HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(T resourceType, Object identifier, String detail) {
        super(resourceType, buildMessage(resourceType, identifier) + "：" + detail, HttpStatus.NOT_FOUND);
    }

    private static <T> String buildMessage(T resourceType, Object identifier) {
        return String.format("%s 资源不存在（ID: %s）", resourceType, identifier);
    }
}