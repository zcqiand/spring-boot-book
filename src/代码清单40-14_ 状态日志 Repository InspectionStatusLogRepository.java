package com.lab.inspection.repository;

import com.lab.inspection.entity.InspectionStatus;
import com.lab.inspection.entity.InspectionStatusLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InspectionStatusLogRepository extends JpaRepository<InspectionStatusLog, Long> {

    /**
     * 查询任务的状态流转历史
     */
    List<InspectionStatusLog> findByTaskIdOrderByCreatedAtDesc(Long taskId);

    /**
     * 查询操作人的状态变更记录
     */
    Page<InspectionStatusLog> findByOperatorIdOrderByCreatedAtDesc(Long operatorId, Pageable pageable);

    /**
     * 查询指定时间范围内的状态变更记录
     */
    @Query("SELECT l FROM InspectionStatusLog l WHERE l.createdAt BETWEEN :start AND :end ORDER BY l.createdAt DESC")
    List<InspectionStatusLog> findByTimeRange(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);

    /**
     * 统计各状态转换的次数
     */
    @Query("SELECT l.fromStatus, l.toStatus, COUNT(l) FROM InspectionStatusLog l GROUP BY l.fromStatus, l.toStatus")
    List<Object[]> countTransitions();
}