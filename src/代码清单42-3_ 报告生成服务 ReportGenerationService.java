package com.lab.report.service;

import com.lab.report.entity.InspectionReport;
import com.lab.report.entity.ReportTemplate;
import com.lab.report.repository.*;
import com.lab.inspection.entity.InspectionTask;
import com.lab.inspection.entity.InspectionItem;
import com.lab.inspection.repository.InspectionTaskRepository;
import com.lab.equipment.entity.Equipment;
import com.lab.equipment.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportGenerationService {

    private final InspectionReportRepository reportRepository;
    private final ReportTemplateRepository templateRepository;
    private final InspectionTaskRepository taskRepository;
    private final EquipmentRepository equipmentRepository;
    private final TemplateRenderService renderService;

    /**
     * 生成检测报告
     */
    @Transactional
    public InspectionReport generateReport(Long taskId, Long creatorId) {
        InspectionTask task = taskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));

        if (task.getStatus() != com.lab.inspection.entity.InspectionStatus.COMPLETED) {
            throw new IllegalStateException("任务未完成，无法生成报告");
        }

        ReportTemplate template = templateRepository.findById(task.getTemplateId())
            .orElseThrow(() -> new IllegalArgumentException("模板不存在"));

        Map<String, Object> data = buildReportData(task);

        String content = renderService.render(template, data);

        String reportNo = generateReportNo();

        InspectionReport report = InspectionReport.builder()
            .reportNo(reportNo)
            .taskId(taskId)
            .title("检测报告-" + task.getTitle())
            .status("DRAFT")
            .templateId(template.getId())
            .content(content)
            .createdBy(creatorId)
            .build();

        report = reportRepository.save(report);
        log.info("报告生成成功: {}", reportNo);

        return report;
    }

    private Map<String, Object> buildReportData(InspectionTask task) {
        Map<String, Object> data = new HashMap<>();

        data.put("reportNo", generateReportNo());
        data.put("title", task.getTitle());
        data.put("taskNo", task.getTaskNo());
        data.put("taskDescription", task.getDescription());
        data.put("priority", task.getPriority().getDescription());
        data.put("createdAt", task.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        data.put("completedAt", task.getCompletedAt() != null ?
            task.getCompletedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "");

        data.put("items", task.getItems().stream().map(this::buildItemData).toList());

        if (task.getAssigneeId() != null) {
            data.put("assigneeName", getUserName(task.getAssigneeId()));
        }

        if (task.getLabId() != null) {
            data.put("labName", getLabName(task.getLabId()));
        }

        return data;
    }

    private Map<String, Object> buildItemData(InspectionItem item) {
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", item.getItemName());
        itemData.put("spec", item.getSpec());
        itemData.put("result", item.getResult());
        itemData.put("isPassed", item.getIsPassed() != null ?
            (item.getIsPassed() ? "合格" : "不合格") : "待检");
        return itemData;
    }

    private String generateReportNo() {
        return "RPT-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" +
            String.format("%04d", new java.util.Random().nextInt(9999));
    }

    private String getUserName(Long userId) {
        return "检测员";
    }

    private String getLabName(Long labId) {
        return "中心实验室";
    }
}