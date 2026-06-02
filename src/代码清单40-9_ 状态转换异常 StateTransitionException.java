package com.lab.inspection.exception;

import com.lab.inspection.entity.InspectionStatus;
import com.lab.inspection.entity.InspectionTask;

/**
 * 状态转换异常
 */
public class StateTransitionException extends RuntimeException {

    private final InspectionStatus fromStatus;
    private final InspectionStatus toStatus;
    private final String taskNo;

    public StateTransitionException(InspectionTask task, InspectionStatus toStatus, String message) {
        super(message);
        this.taskNo = task.getTaskNo();
        this.fromStatus = task.getStatus();
        this.toStatus = toStatus;
    }

    public StateTransitionException(InspectionStatus fromStatus, InspectionStatus toStatus, String message) {
        super(message);
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.taskNo = null;
    }

    public InspectionStatus getFromStatus() {
        return fromStatus;
    }

    public InspectionStatus getToStatus() {
        return toStatus;
    }

    public String getTaskNo() {
        return taskNo;
    }
}