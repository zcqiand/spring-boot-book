package com.lab.inspection.entity;

/**
 * 任务优先级枚举
 */
public enum TaskPriority {
    LOW(1, "低"),
    MEDIUM(2, "中"),
    HIGH(3, "高"),
    URGENT(4, "紧急");

    private final int level;
    private final String description;

    TaskPriority(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 判断是否高于指定优先级
     */
    public boolean isHigherThan(TaskPriority other) {
        return this.level > other.level;
    }

    /**
     * 判断是否为紧急任务
     */
    public boolean isUrgent() {
        return this == URGENT || this == HIGH;
    }
}