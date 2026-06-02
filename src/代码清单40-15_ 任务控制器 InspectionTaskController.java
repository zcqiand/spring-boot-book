package com.lab.inspection.controller;

import com.lab.inspection.dto.*;
import com.lab.inspection.entity.InspectionStatus;
import com.lab.inspection.entity.InspectionTask;
import com.lab.inspection.service.InspectionTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inspection/tasks")
@RequiredArgsConstructor
public class InspectionTaskController {

    private final InspectionTaskService taskService;

    /**
     * 创建检测任务
     */
    @PostMapping
    @PreAuthorize("hasAuthority('inspection:task:create')")
    public ResponseEntity<InspectionTask> createTask(@RequestBody CreateTaskRequest request) {
        Long creatorId = getCurrentUserId();
        InspectionTask task = taskService.createTask(request, creatorId);
        return ResponseEntity.ok(task);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('inspection:task:read')")
    public ResponseEntity<InspectionTask> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    /**
     * 分页查询任务
     */
    @GetMapping
    @PreAuthorize("hasAuthority('inspection:task:read')")
    public ResponseEntity<Page<InspectionTask>> queryTasks(
            TaskQueryCriteria criteria,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(taskService.queryTasks(criteria, pageable));
    }

    /**
     * 指派任务
     */
    @PostMapping("/{id}/assign")
    @PreAuthorize("hasAuthority('inspection:task:assign')")
    public ResponseEntity<InspectionTask> assignTask(
            @PathVariable Long id,
            @RequestParam Long assigneeId) {
        Long operatorId = getCurrentUserId();
        return ResponseEntity.ok(taskService.assignTask(id, assigneeId, operatorId));
    }

    /**
     * 开始执行任务
     */
    @PostMapping("/{id}/start")
    @PreAuthorize("hasAuthority('inspection:task:execute')")
    public ResponseEntity<InspectionTask> startTask(@PathVariable Long id) {
        Long operatorId = getCurrentUserId();
        return ResponseEntity.ok(taskService.startTask(id, operatorId));
    }

    /**
     * 提交审核
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('inspection:task:execute')")
    public ResponseEntity<InspectionTask> submitForReview(@PathVariable Long id) {
        Long operatorId = getCurrentUserId();
        return ResponseEntity.ok(taskService.submitForReview(id, operatorId));
    }

    /**
     * 审批任务
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('inspection:task:approve')")
    public ResponseEntity<InspectionTask> approveTask(
            @PathVariable Long id,
            @RequestParam boolean approved,
            @RequestParam(required = false) String reason) {
        Long operatorId = getCurrentUserId();
        return ResponseEntity.ok(taskService.approveTask(id, approved, reason, operatorId));
    }

    /**
     * 完成任务
     */
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAuthority('inspection:task:execute')")
    public ResponseEntity<InspectionTask> completeTask(@PathVariable Long id) {
        Long operatorId = getCurrentUserId();
        return ResponseEntity.ok(taskService.completeTask(id, operatorId));
    }

    /**
     * 获取可用状态转换
     */
    @GetMapping("/{id}/transitions")
    @PreAuthorize("hasAuthority('inspection:task:read')")
    public ResponseEntity<List<InspectionStatus>> getAvailableTransitions(@PathVariable Long id) {
        Long operatorId = getCurrentUserId();
        return ResponseEntity.ok(taskService.getAvailableTransitions(id, operatorId));
    }

    /**
     * 取消任务
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('inspection:task:create')")
    public ResponseEntity<Void> cancelTask(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        Long operatorId = getCurrentUserId();
        taskService.cancelTask(id, operatorId, reason);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取当前用户ID（从安全上下文）
     */
    private Long getCurrentUserId() {
        // 实际实现：从 SecurityContext 获取当前用户ID
        return 1L;
    }
}