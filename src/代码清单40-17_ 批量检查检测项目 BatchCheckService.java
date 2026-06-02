package com.lab.inspection.service;

import com.lab.inspection.entity.InspectionItem;
import com.lab.inspection.entity.InspectionTask;
import com.lab.inspection.repository.InspectionItemRepository;
import com.lab.inspection.repository.InspectionTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 批量检查服务
 * 演示如何在状态机基础上实现批量操作
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BatchCheckService {

    private final InspectionItemRepository itemRepository;
    private final InspectionTaskRepository taskRepository;
    private final InspectionTaskService taskService;

    /**
     * 批量检查检测项目
     */
    @Transactional
    public InspectionTask batchCheckItems(
            Long taskId,
            Map<Long, CheckResult> itemResults,
            Long checkerId) {

        InspectionTask task = taskRepository.findByIdWithItems(taskId)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));

        // 批量更新检查结果
        int passCount = 0;
        int failCount = 0;

        for (Map.Entry<Long, CheckResult> entry : itemResults.entrySet()) {
            Long itemId = entry.getKey();
            CheckResult result = entry.getValue();

            InspectionItem item = task.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("检测项目不存在: " + itemId));

            item.markAsChecked(checkerId, result.passed(), result.result());

            if (Boolean.TRUE.equals(result.passed())) {
                passCount++;
            } else {
                failCount++;
            }
        }

        itemRepository.saveAll(task.getItems());

        log.info("批量检查完成 - 任务：{}，合格：{}，不合格：{}",
            task.getTaskNo(), passCount, failCount);

        return task;
    }

    /**
     * 一键通过（全部合格）
     */
    @Transactional
    public InspectionTask markAllAsPassed(Long taskId, Long checkerId, String result) {
        InspectionTask task = taskRepository.findByIdWithItems(taskId)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));

        for (InspectionItem item : task.getItems()) {
            if (!item.isChecked()) {
                item.markAsChecked(checkerId, true, result);
            }
        }

        itemRepository.saveAll(task.getItems());

        log.info("任务 {} 所有检测项目已标记为合格", task.getTaskNo());
        return task;
    }

    /**
     * 一键驳回（全部不合格）
     */
    @Transactional
    public InspectionTask markAllAsFailed(Long taskId, Long checkerId, String result) {
        InspectionTask task = taskRepository.findByIdWithItems(taskId)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));

        for (InspectionItem item : task.getItems()) {
            if (!item.isChecked()) {
                item.markAsChecked(checkerId, false, result);
            }
        }

        itemRepository.saveAll(task.getItems());

        log.info("任务 {} 所有检测项目已标记为不合格", task.getTaskNo());
        return task;
    }

    /**
     * 检查结果记录
     */
    public record CheckResult(Boolean passed, String result) {}
}