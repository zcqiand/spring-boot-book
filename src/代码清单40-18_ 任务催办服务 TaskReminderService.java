package com.lab.inspection.service;

import com.lab.inspection.entity.InspectionStatus;
import com.lab.inspection.entity.InspectionTask;
import com.lab.inspection.repository.InspectionTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 任务催办服务
 * 基于状态机实现智能催办
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskReminderService {

    private final InspectionTaskRepository taskRepository;
    private final NotificationService notificationService;

    /**
     * 每天早上9点检查待催办任务
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void checkAndRemindTasks() {
        log.info("开始执行任务催办检查");

        // 1. 检查超时未开始的任务
        remindUnstartedTasks();

        // 2. 检查即将到期的任务
        remindUpcomingTasks();

        // 3. 检查超期未完成的任务
        remindOverdueTasks();

        log.info("任务催办检查完成");
    }

    /**
     * 催办超时未开始的任务
     * 已指派超过24小时但状态仍是 ASSIGNED 的任务
     */
    private void remindUnstartedTasks() {
        List<InspectionTask> tasks = taskRepository.findByStatus(InspectionStatus.ASSIGNED);

        for (InspectionTask task : tasks) {
            long hoursSinceAssigned = ChronoUnit.HOURS.between(
                task.getUpdatedAt(), LocalDate.now().atStartOfDay());

            if (hoursSinceAssigned >= 24) {
                String message = String.format(
                    "任务「%s」已指派超过24小时，请尽快开始执行。\n" +
                    "任务编号：%s\n截止日期：%s",
                    task.getTitle(), task.getTaskNo(), task.getDueDate());

                notificationService.sendToUser(task.getAssigneeId(),
                    "【催办】检测任务尚未开始", message);

                log.info("催办未开始任务：{}", task.getTaskNo());
            }
        }
    }

    /**
     * 催办即将到期的任务
     * 3天内到期且未完成的任务
     */
    private void remindUpcomingTasks() {
        LocalDate threeDaysLater = LocalDate.now().plusDays(3);
        List<InspectionTask> allActiveTasks = taskRepository.findByStatusIn(
            List.of(InspectionStatus.ASSIGNED, InspectionStatus.IN_PROGRESS));

        for (InspectionTask task : allActiveTasks) {
            if (task.getDueDate() != null &&
                !task.getDueDate().isAfter(threeDaysLater) &&
                !task.getDueDate().isBefore(LocalDate.now())) {

                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), task.getDueDate());

                String message = String.format(
                    "任务「%s」即将到期（还剩%d天）。\n" +
                    "任务编号：%s\n截止日期：%s\n" +
                    "请合理安排时间，确保按时完成。",
                    task.getTitle(), daysLeft, task.getTaskNo(), task.getDueDate());

                notificationService.sendToUser(task.getAssigneeId(),
                    "【提醒】检测任务即将到期", message);

                log.info("催办即将到期任务：{}，剩余{}天", task.getTaskNo(), daysLeft);
            }
        }
    }

    /**
     * 催办已超期的任务
     */
    private void remindOverdueTasks() {
        LocalDate today = LocalDate.now();
        List<InspectionTask> allActiveTasks = taskRepository.findByStatusIn(
            List.of(InspectionStatus.ASSIGNED, InspectionStatus.IN_PROGRESS, InspectionStatus.PENDING_REVIEW));

        for (InspectionTask task : allActiveTasks) {
            if (task.getDueDate() != null && task.getDueDate().isBefore(today)) {
                long daysOverdue = ChronoUnit.DAYS.between(task.getDueDate(), today);

                String message = String.format(
                    "任务「%s」已超期%d天，请尽快处理！\n" +
                    "任务编号：%s\n原截止日期：%s\n" +
                    "当前状态：%s",
                    task.getTitle(), daysOverdue, task.getTaskNo(),
                    task.getDueDate(), task.getStatus().getDescription());

                // 催办执行人
                if (task.getAssigneeId() != null) {
                    notificationService.sendToUser(task.getAssigneeId(),
                        "【超期】检测任务已超期", message);
                }

                // 同时催办创建人
                notificationService.sendToUser(task.getCreatedBy(),
                    "【超期】您创建的任务已超期", message);

                log.warn("催办超期任务：{}，超期{}天", task.getTaskNo(), daysOverdue);
            }
        }
    }
}