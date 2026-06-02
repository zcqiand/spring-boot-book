package com.lab.inspection.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_status_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 30)
    private InspectionStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 30)
    private InspectionStatus toStatus;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(length = 500)
    private String reason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 创建状态变更记录
     */
    public static InspectionStatusLog create(
            Long taskId,
            InspectionStatus from,
            InspectionStatus to,
            Long operatorId,
            String reason) {
        return InspectionStatusLog.builder()
            .taskId(taskId)
            .fromStatus(from)
            .toStatus(to)
            .operatorId(operatorId)
            .reason(reason)
            .build();
    }
}