package com.lab.inspection.service;

import com.lab.inspection.entity.*;
import com.lab.inspection.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class InspectionTaskServiceImpl implements InspectionTaskService {

    private final InspectionTaskRepository taskRepository;
    private final InspectionItemRepository itemRepository;
    private final InspectionStatusLogRepository logRepository;
    private final StateTransitionGuard transitionGuard;

    @Override
    @Transactional
    public InspectionTask createTask(CreateTaskRequest request, Long creatorId) {
        InspectionTask task = InspectionTask.builder()
            .taskNo(generateTaskNo())
            .title(request.getTitle())
            .description(request.getDescription())
            .status(InspectionStatus.CREATED)
            .priority(request.getPriority())
            .labId(request.getLabId())
            .createdBy(creatorId)
            .dueDate(request.getDueDate())
            .build();

        task = taskRepository.save(task);
        logStatusChange(task, null, InspectionStatus.CREATED, creatorId, "创建任务");
        return task;
    }

    @Override
    @Transactional
    public InspectionTask assignTask(Long taskId, Long assigneeId, Long operatorId) {
        InspectionTask task = taskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));

        transitionGuard.validateTransition(task, InspectionStatus.ASSIGNED);

        task.setAssigneeId(assigneeId);
        task.transitionTo(InspectionStatus.ASSIGNED);
        task = taskRepository.save(task);

        logStatusChange(task, InspectionStatus.CREATED, InspectionStatus.ASSIGNED, operatorId, "指派任务");
        return task;
    }

    @Override
    @Transactional
    public InspectionTask startTask(Long taskId, Long operatorId) {
        InspectionTask task = taskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));

        transitionGuard.validateTransition(task, InspectionStatus.IN_PROGRESS);
        task.transitionTo(InspectionStatus.IN_PROGRESS);
        task = taskRepository.save(task);

        logStatusChange(task, InspectionStatus.ASSIGNED, InspectionStatus.IN_PROGRESS, operatorId, "开始执行");
        return task;
    }

    @Override
    @Transactional
    public InspectionTask submitForReview(Long taskId, Long operatorId) {
        InspectionTask task = taskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));

        transitionGuard.validateTransition(task, InspectionStatus.PENDING_REVIEW);
        task.transitionTo(InspectionStatus.PENDING_REVIEW);
        task = taskRepository.save(task);

        logStatusChange(task, InspectionStatus.IN_PROGRESS, InspectionStatus.PENDING_REVIEW, operatorId, "提交审核");
        return task;
    }

    @Override
    @Transactional
    public InspectionTask approveTask(Long taskId, boolean approved, String reason, Long operatorId) {
        InspectionTask task = taskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));

        if (approved) {
            transitionGuard.validateTransition(task, InspectionStatus.APPROVED);
            task.transitionTo(InspectionStatus.APPROVED);
            logStatusChange(task, InspectionStatus.PENDING_REVIEW, InspectionStatus.APPROVED, operatorId, reason);
        } else {
            transitionGuard.validateTransition(task, InspectionStatus.REJECTED);
            task.transitionTo(InspectionStatus.REJECTED);
            task.setRejectionReason(reason);
            logStatusChange(task, InspectionStatus.PENDING_REVIEW, InspectionStatus.REJECTED, operatorId, reason);
        }

        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public InspectionTask completeTask(Long taskId, Long operatorId) {
        InspectionTask task = taskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));

        transitionGuard.validateTransition(task, InspectionStatus.COMPLETED);
        task.transitionTo(InspectionStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());
        task = taskRepository.save(task);

        logStatusChange(task, InspectionStatus.APPROVED, InspectionStatus.COMPLETED, operatorId, "完成任务");
        return task;
    }

    private void logStatusChange(InspectionTask task, InspectionStatus from, InspectionStatus to, Long operatorId, String reason) {
        InspectionStatusLog log = InspectionStatusLog.builder()
            .taskId(task.getId())
            .fromStatus(from)
            .toStatus(to)
            .operatorId(operatorId)
            .reason(reason)
            .build();
        logRepository.save(log);
    }

    private String generateTaskNo() {
        return "INS-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" +
            String.format("%04d", new Random().nextInt(9999));
    }
}