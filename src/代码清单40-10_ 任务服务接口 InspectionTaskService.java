package com.lab.inspection.service;

import com.lab.inspection.dto.*;
import com.lab.inspection.entity.InspectionStatus;
import com.lab.inspection.entity.InspectionTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 检测任务服务接口
 */
public interface InspectionTaskService {

    /**
     * 创建检测任务
     */
    InspectionTask createTask(CreateTaskRequest request, Long creatorId);

    /**
     * 指派任务
     */
    InspectionTask assignTask(Long taskId, Long assigneeId, Long operatorId);

    /**
     * 开始执行任务
     */
    InspectionTask startTask(Long taskId, Long operatorId);

    /**
     * 提交审核
     */
    InspectionTask submitForReview(Long taskId, Long operatorId);

    /**
     * 审批任务（通过/驳回）
     */
    InspectionTask approveTask(Long taskId, boolean approved, String reason, Long operatorId);

    /**
     * 完成任务
     */
    InspectionTask completeTask(Long taskId, Long operatorId);

    /**
     * 获取任务详情
     */
    InspectionTask getTask(Long taskId);

    /**
     * 分页查询任务
     */
    Page<InspectionTask> queryTasks(TaskQueryCriteria criteria, Pageable pageable);

    /**
     * 获取任务可用的状态转换
     */
    List<InspectionStatus> getAvailableTransitions(Long taskId, Long operatorId);

    /**
     * 取消任务
     */
    void cancelTask(Long taskId, Long operatorId, String reason);
}