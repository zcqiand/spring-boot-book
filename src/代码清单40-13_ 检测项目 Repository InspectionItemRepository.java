package com.lab.inspection.repository;

import com.lab.inspection.entity.InspectionItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionItemRepository extends JpaRepository<InspectionItem, Long> {

    /**
     * 根据任务ID查询所有检测项目
     */
    List<InspectionItem> findByTaskId(Long taskId);

    /**
     * 根据任务ID删除所有检测项目
     */
    void deleteByTaskId(Long taskId);

    /**
     * 查询任务中不合格的项目
     */
    @Query("SELECT i FROM InspectionItem i WHERE i.task.id = :taskId AND i.isPassed = false")
    List<InspectionItem> findFailedItemsByTaskId(@Param("taskId") Long taskId);

    /**
     * 查询任务中已检查的项目
     */
    @Query("SELECT i FROM InspectionItem i WHERE i.task.id = :taskId AND i.isPassed IS NOT NULL")
    List<InspectionItem> findCheckedItemsByTaskId(@Param("taskId") Long taskId);

    /**
     * 统计任务的检测项合格率
     */
    @Query("SELECT COUNT(i) FROM InspectionItem i WHERE i.task.id = :taskId AND i.isPassed = true")
    long countPassedItems(@Param("taskId") Long taskId);

    @Query("SELECT COUNT(i) FROM InspectionItem i WHERE i.task.id = :taskId")
    long countTotalItems(@Param("taskId") Long taskId);
}