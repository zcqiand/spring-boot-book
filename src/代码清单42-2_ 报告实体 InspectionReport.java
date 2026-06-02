package com.lab.report.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_no", nullable = false, unique = true)
    private String reportNo;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column
    private String title;

    @Column(nullable = false)
    private String status = "DRAFT";

    @Column(name = "template_id")
    private Long templateId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "pdf_url", length = 500)
    private String pdfUrl;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean canSubmitForReview() {
        return "DRAFT".equals(status);
    }

    public boolean canApprove() {
        return "PENDING_REVIEW".equals(status);
    }

    public boolean canPublish() {
        return "APPROVED".equals(status);
    }
}