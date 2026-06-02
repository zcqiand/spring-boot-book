package com.xrtech.chapter12.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 任务管理REST控制器
 * 展示多种参数类型的综合使用
 */
@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {

    /**
     * 创建任务
     * 组合使用：路径参数 + Query参数 + 请求体 + 请求头 + Cookie
     *
     * POST /api/projects/{projectId}/tasks?priority=high
     * Headers: X-Operator-Id: user123
     * Cookie: lang=zh-CN
     * Body: {"title": "xxx", "description": "yyy", ...}
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long projectId,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String dueDate,
            @RequestHeader("X-Operator-Id") String operatorId,
            @CookieValue(value = "lang", defaultValue = "en") String lang,
            @Valid @RequestBody CreateTaskRequest request) {

        // 构建响应
        TaskResponse response = new TaskResponse();
        response.setId((long) (Math.random() * 10000));
        response.setProjectId(projectId);
        response.setTitle(request.getTitle());
        response.setDescription(request.getDescription());
        response.setPriority(priority != null ? priority : "normal");
        response.setDueDate(dueDate);
        response.setOperatorId(operatorId);
        response.setLanguage(lang);
        response.setStatus("created");
        response.setCreateTime(LocalDate.now().toString());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/projects/" + projectId + "/tasks/" + response.getId())
                .body(response);
    }

    /**
     * 查询任务列表
     * 支持按优先级和截止日期筛选
     *
     * GET /api/projects/{projectId}/tasks?priority=high&dueDate=2024-12-31
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @PathVariable Long projectId,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String dueDate) {

        // 返回模拟数据
        return ResponseEntity.ok(List.of());
    }

    /**
     * 获取单个任务详情
     *
     * GET /api/projects/{projectId}/tasks/{taskId}
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {

        TaskResponse response = new TaskResponse();
        response.setId(taskId);
        response.setProjectId(projectId);
        response.setTitle("示例任务");
        response.setPriority("normal");
        response.setStatus("in_progress");

        return ResponseEntity.ok(response);
    }
}

/**
 * 任务创建请求DTO
 */
class CreateTaskRequest {
    @NotBlank(message = "任务标题不能为空")
    private String title;

    private String description;

    private String assignee;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }
}

/**
 * 任务响应DTO
 */
class TaskResponse {
    private Long id;
    private Long projectId;
    private String title;
    private String description;
    private String priority;
    private String dueDate;
    private String operatorId;
    private String language;
    private String status;
    private String createTime;

    // getter和setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
}