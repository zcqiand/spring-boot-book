package com.lab.integration.service;

import com.lab.inspection.entity.InspectionTask;
import com.lab.inspection.entity.InspectionItem;
import com.lab.api.dto.TaskDTO;
import com.lab.api.dto.ItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataTransformService {

    public TaskDTO toTaskDTO(InspectionTask task) {
        TaskDTO dto = TaskDTO.builder()
            .taskId(task.getId())
            .taskNo(task.getTaskNo())
            .title(task.getTitle())
            .description(task.getDescription())
            .status(task.getStatus().name())
            .statusDesc(task.getStatus().getDescription())
            .priority(task.getPriority().name())
            .labId(task.getLabId())
            .assigneeId(task.getAssigneeId())
            .createdBy(task.getCreatedBy())
            .createdAt(task.getCreatedAt())
            .dueDate(task.getDueDate())
            .completedAt(task.getCompletedAt())
            .build();

        if (task.getItems() != null) {
            dto.setItems(task.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList()));
        }

        return dto;
    }

    public ItemDTO toItemDTO(InspectionItem item) {
        return ItemDTO.builder()
            .itemId(item.getId())
            .itemName(item.getItemName())
            .spec(item.getSpec())
            .result(item.getResult())
            .isPassed(item.getIsPassed())
            .checkedAt(item.getCheckedAt())
            .build();
    }

    public InspectionTask toTaskEntity(CreateTaskRequest request) {
        InspectionTask task = InspectionTask.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .labId(request.getLabId())
            .createdBy(request.getCreatorId())
            .priority(request.getPriority())
            .build();

        if (request.getItems() != null) {
            List<InspectionItem> items = request.getItems().stream()
                .map(itemReq -> InspectionItem.builder()
                    .itemName(itemReq.getItemName())
                    .spec(itemReq.getSpec())
                    .build())
                .collect(Collectors.toList());
            items.forEach(task::addItem);
        }

        return task;
    }
}