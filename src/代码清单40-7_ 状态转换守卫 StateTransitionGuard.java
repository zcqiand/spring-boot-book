package com.lab.inspection.service;

import com.lab.inspection.entity.InspectionStatus;
import com.lab.inspection.entity.InspectionTask;
import com.lab.inspection.exception.StateTransitionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

/**
 * 状态转换守卫
 * 在执行状态转换前进行条件校验
 */
@Component
@Slf4j
public class StateTransitionGuard {

    /**
     * 校验是否允许进行状态转换
     */
    public void validateTransition(InspectionTask task, InspectionStatus targetStatus, Long operatorId) {
        // 检查目标状态是否在允许的后继状态中
        if (!task.canTransitionTo(targetStatus)) {
            throw new StateTransitionException(task, targetStatus,
                String.format("状态 %s 不允许转换为 %s", task.getStatus(), targetStatus));
        }

        // 根据目标状态执行特定校验
        switch (targetStatus) {
            case ASSIGNED -> validateAssign(task, operatorId);
            case IN_PROGRESS -> validateStart(task, operatorId);
            case PENDING_REVIEW -> validateSubmitForReview(task, operatorId);
            case APPROVED, REJECTED -> validateApprove(task, operatorId);
            case COMPLETED -> validateComplete(task, operatorId);
            default -> {}
        }

        log.debug("状态转换校验通过：任务 {} 从 {} 转换为 {}",
            task.getTaskNo(), task.getStatus(), targetStatus);
    }

    /**
     * 校验指派操作
     */
    private void validateAssign(InspectionTask task, Long operatorId) {
        if (task.getAssigneeId() == null) {
            throw new StateTransitionException(task, InspectionStatus.ASSIGNED,
                "指派任务时必须指定检测员");
        }
    }

    /**
     * 校验开始执行
     */
    private void validateStart(InspectionTask task, Long operatorId) {
        // 执行人必须是任务指派人
        if (!task.getAssigneeId().equals(operatorId)) {
            throw new StateTransitionException(task, InspectionStatus.IN_PROGRESS,
                "只有任务指派人才能开始执行");
        }

        // 检查是否有检测项目
        if (task.getItems() == null || task.getItems().isEmpty()) {
            throw new StateTransitionException(task, InspectionStatus.IN_PROGRESS,
                "任务必须包含至少一个检测项目");
        }
    }

    /**
     * 校验提交审核
     */
    private void validateSubmitForReview(InspectionTask task, Long operatorId) {
        // 所有检测项目必须已完成检查
        boolean allChecked = task.getItems().stream()
            .allMatch(item -> item.isChecked());

        if (!allChecked) {
            throw new StateTransitionException(task, InspectionStatus.PENDING_REVIEW,
                "所有检测项目必须完成检查后才能提交审核");
        }

        // 检查是否有不合格项目
        boolean hasFailed = task.getItems().stream()
            .anyMatch(item -> !item.isPassed());

        if (hasFailed) {
            log.warn("任务 {} 存在不合格项目，请确认是否继续提交审核", task.getTaskNo());
        }
    }

    /**
     * 校验审批操作
     */
    private void validateApprove(InspectionTask task, Long operatorId) {
        // 审批人不能是任务执行人（需要分离职责）
        // 实际系统中应通过角色权限控制，此处仅做演示
        if (task.getAssigneeId() != null && task.getAssigneeId().equals(operatorId)) {
            throw new StateTransitionException(task, InspectionStatus.APPROVED,
                "任务执行人不能审批自己的任务");
        }

        // 审核时必须给出驳回原因
        if (task.getStatus() == InspectionStatus.PENDING_REVIEW && task.getRejectionReason() != null) {
            // 如果设置了驳回原因，则状态必须是 REJECTED
        }
    }

    /**
     * 校验完成任务
     */
    private void validateComplete(InspectionTask task, Long operatorId) {
        // 必须是已审批通过的状态
        if (task.getStatus() != InspectionStatus.APPROVED) {
            throw new StateTransitionException(task, InspectionStatus.COMPLETED,
                "只有审批通过的任务才能完成");
        }

        // 检查截止日期
        if (task.getDueDate() != null && LocalDate.now().isAfter(task.getDueDate())) {
            log.warn("任务 {} 已超过截止日期", task.getTaskNo());
        }
    }

    /**
     * 获取操作人可执行的目标状态集合
     */
    public Set<InspectionStatus> getAvailableTransitions(InspectionTask task, Long operatorId) {
        Set<InspectionStatus> validNextStates = task.getStatus().getNextValidStates();

        // 根据权限过滤可用的目标状态
        return validNextStates.stream()
            .filter(status -> hasPermissionForTransition(task, status, operatorId))
            .collect(java.util.stream.Collectors.toSet());
    }

    /**
     * 检查操作人对特定状态转换是否有权限
     */
    private boolean hasPermissionForTransition(
            InspectionTask task,
            InspectionStatus targetStatus,
            Long operatorId) {

        // 创建者可以指派任务
        if (targetStatus == InspectionStatus.ASSIGNED && task.getCreatedBy().equals(operatorId)) {
            return true;
        }

        // 指派人可以开始执行
        if (targetStatus == InspectionStatus.IN_PROGRESS &&
            task.getAssigneeId() != null && task.getAssigneeId().equals(operatorId)) {
            return true;
        }

        // 执行人可以提交审核
        if (targetStatus == InspectionStatus.PENDING_REVIEW &&
            task.getAssigneeId() != null && task.getAssigneeId().equals(operatorId)) {
            return true;
        }

        // 审批人（通常是 MANAGER 或 ADMIN）可以审批
        if ((targetStatus == InspectionStatus.APPROVED || targetStatus == InspectionStatus.REJECTED) &&
            !task.getAssigneeId().equals(operatorId)) {
            return true;
        }

        // 已审批通过的任务可以标记为完成
        if (targetStatus == InspectionStatus.COMPLETED) {
            return true;
        }

        return false;
    }
}