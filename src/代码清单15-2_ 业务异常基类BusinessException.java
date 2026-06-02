package com.xrtech.api.exception;

import org.springframework.http.HttpStatus;

/**
 * 业务异常基类
 * 所有自定义业务异常都应继承此类
 */
public class BusinessException<T> extends RuntimeException {

    private final T errorCode;
    private final HttpStatus httpStatus;

    public BusinessException(T errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public BusinessException(T errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public T getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}