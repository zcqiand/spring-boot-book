package com.lab.security.context;

import lombok.Getter;

/**
 * 实验室上下文 Holder
 * 用于在当前线程中传递用户访问的实验室信息
 */
public class LabContextHolder {

    private static final ThreadLocal<Long> currentLabId = new ThreadLocal<>();
    private static final ThreadLocal<DataScopeContext> dataScopeContext = new ThreadLocal<>();

    @Getter
    public record DataScopeContext(Long userId, String dataScope, String resourceType) {}

    public static void setCurrentLabId(Long labId) {
        currentLabId.set(labId);
    }

    public static Long getCurrentLabId() {
        return currentLabId.get();
    }

    public static void setDataScope(DataScopeContext context) {
        dataScopeContext.set(context);
    }

    public static DataScopeContext getDataScope() {
        return dataScopeContext.get();
    }

    public static void clear() {
        currentLabId.remove();
        dataScopeContext.remove();
    }
}