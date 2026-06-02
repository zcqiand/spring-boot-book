package com.xrtech.api.exception;

/**
 * 业务规则异常
 * 当业务逻辑校验失败（如余额不足、状态不正确）时抛出
 */
public class BusinessRuleException<T> extends BusinessException<T> {

    private boolean logged = false;

    public BusinessRuleException(T errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessRuleException(T errorCode, String message, boolean logged) {
        super(errorCode, message);
        this.logged = logged;
    }

    public boolean isLogged() {
        return logged;
    }

    public void markLogged() {
        this.logged = true;
    }
}