package com.lab.inspection.repository;

import com.lab.inspection.entity.InspectionTask;
import com.lab.inspection.entity.InspectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InspectionTaskRepository extends JpaRepository<InspectionTask, Long> {

    List<InspectionTask> findByStatus(InspectionStatus status);

    List<InspectionTask> findByAssigneeId(Long assigneeId);

    List<InspectionTask> findByLabId(Long labId);

    @Query("SELECT t FROM InspectionTask t WHERE t.status = :status AND t.dueDate < :date")
    List<InspectionTask> findOverdueByStatus(@Param("status") InspectionStatus status, @Param("date") LocalDateTime date);

    @Query("SELECT t FROM InspectionTask t WHERE t.assigneeId = :assigneeId AND t.status IN :statuses")
    List<InspectionTask> findByAssigneeIdAndStatuses(@Param("assigneeId") Long assigneeId, @Param("statuses") List<InspectionStatus> statuses);

    @Query("SELECT COUNT(t) FROM InspectionTask t WHERE t.status = :status AND t.labId = :labId")
    long countByStatusAndLabId(@Param("status") InspectionStatus status, @Param("labId") Long labId);
}