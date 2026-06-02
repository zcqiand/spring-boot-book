package com.lab.report.service;

import com.lab.report.entity.InspectionReport;
import com.lab.report.entity.ReportApprovalLog;
import com.lab.report.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalService {

    private final InspectionReportRepository reportRepository;
    private final ReportApprovalLogRepository approvalLogRepository;

    /**
     * 提交报告审核
     */
    @Transactional
    public InspectionReport submitForReview(Long reportId, Long submitterId) {
        InspectionReport report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("报告不存在"));

        if (!report.canSubmitForReview()) {
            throw new IllegalStateException("报告当前状态不允许提交审核");
        }

        report.setStatus("PENDING_REVIEW");
        report = reportRepository.save(report);

        logApproval(reportId, "SUBMIT", submitterId, null, "提交审核");
        log.info("报告提交审核: {}", report.getReportNo());

        return report;
    }

    /**
     * 一级审核
     */
    @Transactional
    public InspectionReport firstReview(Long reportId, Long reviewerId, boolean approved, String comment) {
        InspectionReport report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("报告不存在"));

        if (!"PENDING_REVIEW".equals(report.getStatus())) {
            throw new IllegalStateException("报告不在待审核状态");
        }

        if (approved) {
            report.setStatus("PENDING_SECOND_REVIEW");
            logApproval(reportId, "FIRST_REVIEW", reviewerId, "APPROVED", comment);
            log.info("报告一级审核通过: {}", report.getReportNo());
        } else {
            report.setStatus("DRAFT");
            logApproval(reportId, "FIRST_REVIEW", reviewerId, "REJECTED", comment);
            log.info("报告一级审核驳回: {}", report.getReportNo());
        }

        return reportRepository.save(report);
    }

    /**
     * 最终批准
     */
    @Transactional
    public InspectionReport approve(Long reportId, Long approverId, boolean approved, String comment) {
        InspectionReport report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("报告不存在"));

        if (!report.canApprove()) {
            throw new IllegalStateException("报告当前状态不允许批准");
        }

        if (approved) {
            report.setStatus("APPROVED");
            logApproval(reportId, "APPROVE", approverId, "APPROVED", comment);
            log.info("报告批准通过: {}", report.getReportNo());
        } else {
            report.setStatus("DRAFT");
            logApproval(reportId, "APPROVE", approverId, "REJECTED", comment);
            log.info("报告批准驳回: {}", report.getReportNo());
        }

        return reportRepository.save(report);
    }

    /**
     * 发布报告
     */
    @Transactional
    public InspectionReport publish(Long reportId, Long publisherId) {
        InspectionReport report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("报告不存在"));

        if (!report.canPublish()) {
            throw new IllegalStateException("报告未经过批准，无法发布");
        }

        report.setStatus("PUBLISHED");
        report.setPublishedAt(LocalDateTime.now());
        report = reportRepository.save(report);

        log.info("报告发布成功: {}", report.getReportNo());
        return report;
    }

    private void logApproval(Long reportId, String type, Long approverId, String result, String comment) {
        ReportApprovalLog log = ReportApprovalLog.builder()
            .reportId(reportId)
            .approvalType(type)
            .approverId(approverId)
            .result(result)
            .comment(comment)
            .build();
        approvalLogRepository.save(log);
    }
}