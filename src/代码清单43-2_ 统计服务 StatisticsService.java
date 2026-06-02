package com.lab.statistics.service;

import com.lab.statistics.repository.DailyStatisticsRepository;
import com.lab.inspection.entity.InspectionStatus;
import com.lab.inspection.repository.InspectionTaskRepository;
import com.lab.inspection.repository.InspectionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final DailyStatisticsRepository statisticsRepository;
    private final InspectionTaskRepository taskRepository;
    private final InspectionItemRepository itemRepository;

    /**
     * 获取工作台概览数据
     */
    public DashboardOverview getDashboardOverview(Long labId) {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        int taskCount = taskRepository.countByLabIdAndDateRange(labId, startOfMonth, endOfMonth);
        int completedCount = taskRepository.countByLabIdAndStatusAndDateRange(
            labId, InspectionStatus.COMPLETED, startOfMonth, endOfMonth);
        int itemCount = itemRepository.countByLabIdAndDateRange(labId, startOfMonth, endOfMonth);
        int passedCount = itemRepository.countByLabIdAndPassedAndDateRange(labId, true, startOfMonth, endOfMonth);

        double completionRate = taskCount > 0 ? (double) completedCount / taskCount * 100 : 0;
        double passRate = itemCount > 0 ? (double) passedCount / itemCount * 100 : 0;

        return new DashboardOverview(taskCount, completedCount, itemCount, passedCount,
            completionRate, passRate);
    }

    /**
     * 获取任务统计（按状态分布）
     */
    public List<TaskStatusCount> getTaskStatusDistribution(Long labId) {
        return taskRepository.countByStatusGroup(labId);
    }

    /**
     * 获取工作量趋势（近30天）
     */
    public List<DailyCount> getWorkloadTrend(Long labId, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        return taskRepository.countByDateRange(labId, startDate, endDate);
    }

    /**
     * 获取合格率趋势（近30天）
     */
    public List<DailyPassRate> getPassRateTrend(Long labId, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        return itemRepository.getPassRateByDateRange(labId, startDate, endDate);
    }

    public record DashboardOverview(
        int taskCount,
        int completedCount,
        int itemCount,
        int passedCount,
        double completionRate,
        double passRate
    ) {}

    public record TaskStatusCount(String status, long count) {}
    public record DailyCount(LocalDate date, long count) {}
    public record DailyPassRate(LocalDate date, double passRate) {}
}