package com.lab.inspection.service;

import com.lab.inspection.entity.InspectionStatus;
import com.lab.inspection.entity.InspectionStatusLog;
import com.lab.inspection.entity.InspectionTask;
import com.lab.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 状态转换动作
 * 在状态转换时执行相应的业务操作
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StateTransitionAction {

    private final NotificationService notificationService;
    private final InspectionStatusLogService statusLogService;

    /**
     * 执行状态转换后的动作
     */
    public void executeAfterTransition(
            InspectionTask task,
            InspectionStatus fromStatus,
            InspectionStatus toStatus,
            Long operatorId) {

        // 记录状态流转日志
        recordStatusChange(task, fromStatus, toStatus, operatorId);

        // 发送通知
        sendNotification(task, fromStatus, toStatus, operatorId);

        // 执行状态特定的操作
        switch (toStatus) {
            case ASSIGNED -> onAssigned(task, operatorId);
            case IN_PROGRESS -> onInProgress(task, operatorId);
            case PENDING_REVIEW -> onPendingReview(task, operatorId);
            case APPROVED -> onApproved(task, operatorId);
            case REJECTED -> onRejected(task, operatorId);
            case COMPLETED -> onCompleted(task, operatorId);
            default -> {}
        }
    }

    private void recordStatusChange(
            InspectionTask task,
            InspectionStatus from,
            InspectionStatus to,
            Long operatorId) {
        InspectionStatusLog log = InspectionStatusLog.create(
            task.getId(), from, to, operatorId,
            String.format("状态从 %s 变更为 %s", from.getDescription(), to.getDescription())
        );
        statusLogService.save(log);
    }

    private void sendNotification(
            InspectionTask task,
            InspectionStatus from,
            InspectionStatus to,
            Long operatorId) {
        String title = String.format("检测任务 %s 状态变更", task.getTaskNo());
        String content = String.format("任务「%s」状态从 [%s] 变更为 [%s]",
            task.getTitle(), from.getDescription(), to.getDescription());

        // 通知相关人员
        switch (to) {
            case ASSIGNED -> {
                // 通知被指派人
                notificationService.sendToUser(task.getAssigneeId(), title, content);
            }
            case PENDING_REVIEW -> {
                // 通知任务创建者
                notificationService.sendToUser(task.getCreatedBy(), title, content);
            }
            case REJECTED -> {
                // 通知任务执行人
                notificationService.sendToUser(task.getAssigneeId(), title,
                    content + "\n驳回原因：" + task.getRejectionReason());
            }
            case COMPLETED -> {
                // 通知任务创建者
                notificationService.sendToUser(task.getCreatedBy(), title,
                    content + "\n任务已完成，请确认。");
            }
            default -> {}
        }
    }

    private void onAssigned(InspectionTask task, Long operatorId) {
        log.info("任务 {} 已被指派给用户 {}", task.getTaskNo(), task.getAssigneeId());
    }

    private void onInProgress(InspectionTask task, Long operatorId) {
        log.info("任务 {} 开始执行", task.getTaskNo());
    }

    private void onPendingReview(InspectionTask task, Long operatorId) {
        log.info("任务 {} 已提交审核", task.getTaskNo());
    }

    private void onApproved(InspectionTask task, Long operatorId) {
        log.info("任务 {} 已通过审核", task.getTaskNo());
    }

    private void onRejected(InspectionTask task, Long operatorId) {
        log.warn("任务 {} 被驳回，原因：{}", task.getTaskNo(), task.getRejectionReason());
    }

    private void onCompleted(InspectionTask task, Long operatorId) {
        task.setCompletedAt(LocalDateTime.now());
        log.info("任务 {} 已完成", task.getTaskNo());
    }

    /**
     * 执行状态转换前的动作
     */
    public void executeBeforeTransition(
            InspectionTask task,
            InspectionStatus targetStatus) {

        switch (targetStatus) {
            case IN_PROGRESS -> prepareForExecution(task);
            case PENDING_REVIEW -> prepareForReview(task);
            default -> {}
        }
    }

    private void prepareForExecution(InspectionTask task) {
        log.debug("准备执行任务：初始化检测环境");
        // 可以在这里初始化检测设备、准备检测材料等
    }

    private void prepareForReview(InspectionTask task) {
        // 汇总检测结果
        long totalItems = task.getItems().size();
        long passedItems = task.getItems().stream()
            .filter(item -> Boolean.TRUE.equals(item.getIsPassed()))
            .count();

        log.debug("任务 {} 审核前汇总：共 {} 项，合格 {} 项",
            task.getTaskNo(), totalItems, passedItems);
    }
}