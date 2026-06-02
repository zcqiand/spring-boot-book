package com.lab.inspection.dto;

import com.lab.inspection.entity.InspectionStatus;
import com.lab.inspection.entity.TaskPriority;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskQueryCriteria {

    private String taskNo;
    private String title;
    private InspectionStatus status;
    private TaskPriority priority;
    private Long assigneeId;
    private Long labId;
    private Long createdBy;
    private LocalDate dueDateFrom;
    private LocalDate dueDateTo;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private Boolean overdueOnly;

    public Specification<com.lab.inspection.entity.InspectionTask> toSpecification() {
        return Specification.where(taskNoLike(taskNo))
            .and(titleLike(title))
            .and(statusEquals(status))
            .and(priorityEquals(priority))
            .and(assigneeIdEquals(assigneeId))
            .and(labIdEquals(labId))
            .and(createdByEquals(createdBy))
            .and(dueDateBetween(dueDateFrom, dueDateTo))
            .and(overdue(overdueOnly));
    }

    private Specification<com.lab.inspection.entity.InspectionTask> taskNoLike(String taskNo) {
        return (root, query, cb) ->
            taskNo == null ? null :
            cb.like(root.get("taskNo"), "%" + taskNo + "%");
    }

    private Specification<com.lab.inspection.entity.InspectionTask> titleLike(String title) {
        return (root, query, cb) ->
            title == null ? null :
            cb.like(root.get("title"), "%" + title + "%");
    }

    private Specification<com.lab.inspection.entity.InspectionTask> statusEquals(InspectionStatus status) {
        return (root, query, cb) ->
            status == null ? null :
            cb.equal(root.get("status"), status);
    }

    private Specification<com.lab.inspection.entity.InspectionTask> priorityEquals(TaskPriority priority) {
        return (root, query, cb) ->
            priority == null ? null :
            cb.equal(root.get("priority"), priority);
    }

    private Specification<com.lab.inspection.entity.InspectionTask> assigneeIdEquals(Long assigneeId) {
        return (root, query, cb) ->
            assigneeId == null ? null :
            cb.equal(root.get("assigneeId"), assigneeId);
    }

    private Specification<com.lab.inspection.entity.InspectionTask> labIdEquals(Long labId) {
        return (root, query, cb) ->
            labId == null ? null :
            cb.equal(root.get("labId"), labId);
    }

    private Specification<com.lab.inspection.entity.InspectionTask> createdByEquals(Long createdBy) {
        return (root, query, cb) ->
            createdBy == null ? null :
            cb.equal(root.get("createdBy"), createdBy);
    }

    private Specification<com.lab.inspection.entity.InspectionTask> dueDateBetween(
            LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
            if (from == null) return cb.lessThanOrEqualTo(root.get("dueDate"), to);
            if (to == null) return cb.greaterThanOrEqualTo(root.get("dueDate"), from);
            return cb.between(root.get("dueDate"), from, to);
        };
    }

    private Specification<com.lab.inspection.entity.InspectionTask> overdue(Boolean overdueOnly) {
        return (root, query, cb) -> {
            if (!Boolean.TRUE.equals(overdueOnly)) return null;
            return cb.and(
                cb.lessThan(root.get("dueDate"), LocalDate.now()),
                cb.notEqual(root.get("status"), InspectionStatus.COMPLETED)
            );
        };
    }
}