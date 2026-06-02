package com.lab.inspection.service;

import com.lab.inspection.entity.InspectionStatus;
import com.lab.inspection.entity.InspectionTask;
import com.lab.inspection.exception.StateTransitionException;
import org.springframework.stereotype.Component;

/**
 * 状态转换守卫
 * 校验状态转换的合法性
 */
@Component
public class StateTransitionGuard {

    /**
     * 校验状态转换是否合法
     */
    public void validateTransition(InspectionTask task, InspectionStatus targetStatus) {
        InspectionStatus currentStatus = task.getStatus();

        if (!currentStatus.canTransitionTo(targetStatus)) {
            throw new StateTransitionException(
                currentStatus, targetStatus,
                String.format("任务%s状态不允许从%s转换到%s",
                    task.getTaskNo(), currentStatus, targetStatus)
            );
        }

        validateBusinessRules(task, targetStatus);
    }

    private void validateBusinessRules(InspectionTask task, InspectionStatus targetStatus) {
        switch (targetStatus) {
            case IN_PROGRESS:
                validateCanStart(task);
                break;
            case PENDING_REVIEW:
                validateCanSubmitForReview(task);
                break;
            case APPROVED:
            case REJECTED:
                validateCanApproveOrReject(task);
                break;
            case COMPLETED:
                validateCanComplete(task);
                break;
        }
    }

    private void validateCanStart(InspectionTask task) {
        if (task.getAssigneeId() == null) {
            throw new StateTransitionException("任务未指派，无法开始执行");
        }
    }

    private void validateCanSubmitForReview(InspectionTask task) {
        if (task.getItems() == null || task.getItems().isEmpty()) {
            throw new StateTransitionException("任务没有检测项目，无法提交审核");
        }
        boolean allChecked = task.getItems().stream()
            .allMatch(item -> item.getIsPassed() != null);
        if (!allChecked) {
            throw new StateTransitionException("还有检测项目未完成检验");
        }
    }

    private void validateCanApproveOrReject(InspectionTask task) {
        if (task.getStatus() != InspectionStatus.PENDING_REVIEW) {
            throw new StateTransitionException("任务不在待审批状态");
        }
    }

    private void validateCanComplete(InspectionTask task) {
        if (task.getStatus() != InspectionStatus.APPROVED) {
            throw new StateTransitionException("任务未通过审批，无法完成");
        }
    }
}