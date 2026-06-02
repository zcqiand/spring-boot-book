package com.lab.inspection.repository;

import com.lab.inspection.dto.TaskQueryCriteria;
import com.lab.inspection.entity.InspectionStatus;
import com.lab.inspection.entity.InspectionTask;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface InspectionTaskRepository extends JpaRepository<InspectionTask, Long>,
    JpaSpecificationExecutor<InspectionTask> {

    /**
     * 根据任务编号查询
     */
    Optional<InspectionTask> findByTaskNo(String taskNo);

    /**
     * 根据状态查询任务
     */
    List<InspectionTask> findByStatus(InspectionStatus status);

    /**
     * 根据状态列表查询任务
     */
    List<InspectionTask> findByStatusIn(List<InspectionStatus> statuses);

    /**
     * 根据指派人查询任务
     */
    List<InspectionTask> findByAssigneeId(Long assigneeId);

    /**
     * 根据指派人和状态查询任务
     */
    List<InspectionTask> findByAssigneeIdAndStatus(Long assigneeId, InspectionStatus status);

    /**
     * 根据实验室查询任务
     */
    List<InspectionTask> findByLabId(Long labId);

    /**
     * 查询超过截止日期且未完成的任务
     */
    @Query("SELECT t FROM InspectionTask t WHERE t.dueDate < :date AND t.status NOT IN :completedStatuses")
    List<InspectionTask> findOverdueTasks(
        @Param("date") LocalDate date,
        @Param("completedStatuses") List<InspectionStatus> completedStatuses);

    /**
     * 查询待审核任务（按实验室）
     */
    @Query("SELECT t FROM InspectionTask t WHERE t.labId = :labId AND t.status = 'PENDING_REVIEW'")
    List<InspectionTask> findPendingReviewTasksByLab(@Param("labId") Long labId);

    /**
     * 分页查询我的任务（作为执行人）
     */
    @Query("SELECT t FROM InspectionTask t WHERE t.assigneeId = :userId AND t.status NOT IN :excludeStatuses")
    Page<InspectionTask> findMyTasksAsAssignee(
        @Param("userId") Long userId,
        @Param("excludeStatuses") List<InspectionStatus> excludeStatuses,
        Pageable pageable);

    /**
     * 分页查询我创建的任务
     */
    @Query("SELECT t FROM InspectionTask t WHERE t.createdBy = :userId")
    Page<InspectionTask> findMyTasksAsCreator(
        @Param("userId") Long userId,
        Pageable pageable);

    /**
     * 统计各状态的任务数量
     */
    @Query("SELECT t.status, COUNT(t) FROM InspectionTask t WHERE t.labId = :labId GROUP BY t.status")
    List<Object[]> countByStatusForLab(@Param("labId") Long labId);

    /**
     * 查询带有关联项目的任务
     */
    @Query("SELECT DISTINCT t FROM InspectionTask t LEFT JOIN FETCH t.items WHERE t.id = :id")
    Optional<InspectionTask> findByIdWithItems(@Param("id") Long id);

    /**
     * 根据复杂条件分页查询
     */
    default Page<InspectionTask> findByCriteria(TaskQueryCriteria criteria, Pageable pageable) {
        return findAll(criteria.toSpecification(), pageable);
    }

    /**
     * 检查任务编号是否存在
     */
    boolean existsByTaskNo(String taskNo);
}