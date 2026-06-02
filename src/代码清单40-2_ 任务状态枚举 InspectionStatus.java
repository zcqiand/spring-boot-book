package com.lab.inspection.entity;

/**
 * 检测任务状态枚举
 */
public enum InspectionStatus {
    CREATED("已创建"),
    ASSIGNED("已指派"),
    IN_PROGRESS("进行中"),
    PENDING_REVIEW("待审批"),
    APPROVED("已审批"),
    REJECTED("已驳回"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;

    InspectionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 获取当前状态允许的下一状态列表
     * 这是状态机规则的核心定义
     */
    public Set<InspectionStatus> getNextValidStates() {
        return switch (this) {
            case CREATED -> Set.of(ASSIGNED, CANCELLED);
            case ASSIGNED -> Set.of(IN_PROGRESS, CANCELLED);
            case IN_PROGRESS -> Set.of(PENDING_REVIEW, CANCELLED);
            case PENDING_REVIEW -> Set.of(APPROVED, REJECTED);
            case REJECTED -> Set.of(IN_PROGRESS);
            case APPROVED -> Set.of(COMPLETED);
            case COMPLETED, CANCELLED -> Collections.emptySet();
        };
    }

    /**
     * 检查是否可以转换到目标状态
     */
    public boolean canTransitionTo(InspectionStatus target) {
        return getNextValidStates().contains(target);
    }

    /**
     * 是否为终态
     * 终态是不可再转换的状态
     */
    public boolean isFinalState() {
        return this == COMPLETED || this == CANCELLED;
    }
}