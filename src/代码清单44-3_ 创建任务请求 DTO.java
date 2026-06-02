package com.lab.api.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "任务标题不能为空")
    @Size(max = 200, message = "任务标题不能超过200字符")
    private String title;

    @Size(max = 2000, message = "任务描述不能超过2000字符")
    private String description;

    @NotNull(message = "实验室ID不能为空")
    private Long labId;

    @NotNull(message = "创建人ID不能为空")
    private Long creatorId;

    @NotNull(message = "优先级不能为空")
    private TaskPriority priority;

    private String dueDate;

    private List<CreateItemRequest> items;

    @Data
    public static class CreateItemRequest {
        @NotBlank(message = "检测项目名称不能为空")
        private String itemName;

        @Size(max = 500, message = "规格不能超过500字符")
        private String spec;
    }
}