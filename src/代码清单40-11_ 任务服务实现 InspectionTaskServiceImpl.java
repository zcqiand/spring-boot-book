package com.lab.inspection.service;

import com.lab.inspection.dto.*;
import com.lab.inspection.entity.*;
import com.lab.inspection.exception.*;
import com.lab.inspection.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InspectionTaskServiceImpl implements InspectionTaskService {

    private final InspectionTaskRepository taskRepository;
    private final InspectionItemRepository itemRepository;
    private final InspectionStatusLogRepository statusLogRepository;
    private final StateTransitionGuard transitionGuard;
    private final StateTransitionAction transitionAction;
    private final SequenceGenerator sequenceGenerator;

    @Override
    public InspectionTask createTask(CreateTaskRequest request, Long creatorId) {
        // 生成任务编号
        String taskNo = generateTaskNo();

        // 创建任务
        InspectionTask task = InspectionTask.builder()
            .taskNo(taskNo)
            .title(request.getTitle())
            .description(request.getDescription())
            .status(InspectionStatus.CREATED)
            .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM)
            .labId(request.getLabId())
            .createdBy(creatorId)
            .dueDate(request.getDueDate())
            .build();

        // 添加检测项目
        if (request.getItems() != null) {
            for (CreateItemRequest itemRequest : request.getItems()) {
                InspectionItem item = InspectionItem.builder()
                    .itemName(itemRequest.getItemName())
                    .spec(itemRequest.getSpec())
                    .build();
                task.addItem(item);
            }
        }

        task = taskRepository.save(task);

        // 记录状态日志
        InspectionStatusLog statusLog = InspectionStatusLog.create(
            task.getId(), null, InspectionStatus.CREATED, creatorId, "创建任务");
        statusLogRepository.save(statusLog);

        log.info("创建检测任务：{}，创建人：{}", taskNo, creatorId);
        return task;
    }

    @Override
    public InspectionTask assignTask(Long taskId, Long assigneeId, Long operatorId) {
        InspectionTask task = getTaskEntity(taskId);

        // 保存驳回原因（如果有）
        if (task.getStatus() == InspectionStatus.REJECTED) {
            task.setRejectionReason(null);
        }

        task.setAssigneeId(assigneeId);

        return executeTransition(task, InspectionStatus.ASSIGNED, operatorId);
    }

    @Override
    public InspectionTask startTask(Long taskId, Long operatorId) {
        InspectionTask task = getTaskEntity(taskId);
        return executeTransition(task, InspectionStatus.IN_PROGRESS, operatorId);
    }

    @Override
    public InspectionTask submitForReview(Long taskId, Long operatorId) {
        InspectionTask task = getTaskEntity(taskId);
        return executeTransition(task, InspectionStatus.PENDING_REVIEW, operatorId);
    }

    @Override
    public InspectionTask approveTask(Long taskId, boolean approved, String reason, Long operatorId) {
        InspectionTask task = getTaskEntity(taskId);

        if (approved) {
            return executeTransition(task, InspectionStatus.APPROVED, operatorId);
        } else {
            if (reason == null || reason.isBlank()) {
                throw new IllegalArgumentException("驳回任务必须提供原因");
            }
            task.setRejectionReason(reason);
            return executeTransition(task, InspectionStatus.REJECTED, operatorId);
        }
    }

    @Override
    public InspectionTask completeTask(Long taskId, Long operatorId) {
        InspectionTask task = getTaskEntity(taskId);
        return executeTransition(task, InspectionStatus.COMPLETED, operatorId);
    }

    @Override
    @Transactional(readOnly = true)
    public InspectionTask getTask(Long taskId) {
        return getTaskEntity(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InspectionTask> queryTasks(TaskQueryCriteria criteria, Pageable pageable) {
        return taskRepository.findByCriteria(criteria, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InspectionStatus> getAvailableTransitions(Long taskId, Long operatorId) {
        InspectionTask task = getTaskEntity(taskId);
        return new ArrayList<>(transitionGuard.getAvailableTransitions(task, operatorId));
    }

    @Override
    public void cancelTask(Long taskId, Long operatorId, String reason) {
        InspectionTask task = getTaskEntity(taskId);

        if (!task.canTransitionTo(InspectionStatus.CREATED)) {
            throw new StateTransitionException(task, task.getStatus(),
                "当前状态不允许取消任务");
        }

        InspectionStatus fromStatus = task.getStatus();
        task.setStatus(InspectionStatus.CREATED);
        task.setAssigneeId(null);

        taskRepository.save(task);

        // 记录日志
        InspectionStatusLog statusLog = InspectionStatusLog.create(
            taskId, fromStatus, InspectionStatus.CREATED, operatorId,
            "取消任务: " + reason);
        statusLogRepository.save(statusLog);

        log.info("任务 {} 已被取消", task.getTaskNo());
    }

    /**
     * 执行状态转换
     */
    private InspectionTask executeTransition(
            InspectionTask task,
            InspectionStatus targetStatus,
            Long operatorId) {

        InspectionStatus fromStatus = task.getStatus();

        // 校验转换
        transitionGuard.validateTransition(task, targetStatus, operatorId);

        // 执行转换前动作
        transitionAction.executeBeforeTransition(task, targetStatus);

        // 执行状态转换
        task.transitionTo(targetStatus);
        task = taskRepository.save(task);

        // 执行转换后动作
        transitionAction.executeAfterTransition(task, fromStatus, targetStatus, operatorId);

        log.info("任务 {} 状态从 {} 转换为 {}，操作人：{}",
            task.getTaskNo(), fromStatus, targetStatus, operatorId);

        return task;
    }

    private InspectionTask getTaskEntity(Long taskId) {
        return taskRepository.findByIdWithItems(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));
    }

    private String generateTaskNo() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sequence = sequenceGenerator.next("INS-" + datePrefix);
        return "INS-" + datePrefix + "-" + sequence;
    }
}