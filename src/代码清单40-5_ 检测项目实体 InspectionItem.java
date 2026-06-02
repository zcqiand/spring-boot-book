package com.lab.inspection.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private InspectionTask task;

    @Column(name = "item_name", nullable = false, length = 200)
    private String itemName;

    @Column(length = 500)
    private String spec;

    @Column(columnDefinition = "TEXT")
    private String result;

    @Column(name = "is_passed")
    private Boolean isPassed;  // null-未检, true-合格, false-不合格

    @Column(name = "checked_by")
    private Long checkedBy;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @Column(length = 500)
    private String remark;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 标记为已检查
     */
    public void markAsChecked(Long checkerId, Boolean passed, String result) {
        this.checkedBy = checkerId;
        this.checkedAt = LocalDateTime.now();
        this.isPassed = passed;
        this.result = result;
    }

    /**
     * 判断是否已检查
     */
    public boolean isChecked() {
        return isPassed != null;
    }

    /**
     * 判断是否通过
     */
    public boolean isPassed() {
        return Boolean.TRUE.equals(isPassed);
    }
}