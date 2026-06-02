package com.lab.api.controller;

import com.lab.api.common.ApiResponse;
import com.lab.api.dto.*;
import com.lab.inspection.service.InspectionTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskApiController {

    private final InspectionTaskService taskService;

    @GetMapping
    public ApiResponse<List<TaskDTO>> listTasks(
            @RequestParam(required = false) Long labId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        TaskQueryCriteria criteria = TaskQueryCriteria.builder()
            .labId(labId)
            .status(status)
            .page(page)
            .size(size)
            .build();

        List<TaskDTO> tasks = taskService.queryTasks(criteria);
        return ApiResponse.success(tasks);
    }

    @GetMapping("/{taskId}")
    public ApiResponse<TaskDTO> getTask(@PathVariable Long taskId) {
        TaskDTO task = taskService.getTaskDTO(taskId);
        return ApiResponse.success(task);
    }

    @PostMapping
    public ApiResponse<TaskDTO> createTask(@RequestBody @Validated CreateTaskRequest request) {
        TaskDTO task = taskService.createTask(request, request.getCreatorId());
        return ApiResponse.success(task);
    }

    @PutMapping("/{taskId}")
    public ApiResponse<TaskDTO> updateTask(
            @PathVariable Long taskId,
            @RequestBody @Validated UpdateTaskRequest request) {

        TaskDTO task = taskService.updateTask(taskId, request);
        return ApiResponse.success(task);
    }

    @DeleteMapping("/{taskId}")
    public ApiResponse<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ApiResponse.success();
    }

    @PostMapping("/{taskId}/assign")
    public ApiResponse<TaskDTO> assignTask(
            @PathVariable Long taskId,
            @RequestBody @Validated AssignTaskRequest request) {

        TaskDTO task = taskService.assignTask(taskId, request.getAssigneeId(), request.getOperatorId());
        return ApiResponse.success(task);
    }

    @PostMapping("/{taskId}/submit")
    public ApiResponse<TaskDTO> submitTask(
            @PathVariable Long taskId,
            @RequestBody SubmitTaskRequest request) {

        TaskDTO task = taskService.submitForReview(taskId, request.getOperatorId());
        return ApiResponse.success(task);
    }
}