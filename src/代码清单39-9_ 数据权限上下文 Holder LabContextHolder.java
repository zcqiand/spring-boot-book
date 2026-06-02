package com.lab.security.holder;

public class LabContextHolder {

    private static final ThreadLocal<DataPermissionContext> contextHolder = new ThreadLocal<>();

    public static void setDataPermission(Long userId, String dataScope, Long labId) {
        contextHolder.set(new DataPermissionContext(userId, dataScope, labId));
    }

    public static DataPermissionContext getContext() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }

    public record DataPermissionContext(Long userId, String dataScope, Long labId) {}
}