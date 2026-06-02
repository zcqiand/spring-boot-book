package com.lab.repository;

import com.lab.security.context.LabContextHolder;
import com.lab.security.entity.SysUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 带数据权限过滤的实验记录 Repository
 */
@Repository
@Slf4j
public class ExperimentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 查询用户有权限访问的实验记录（应用数据权限过滤）
     */
    @SuppressWarnings("unchecked")
    public List<Experiment> findAccessibleExperiments(Long projectId, int page, int size) {
        String baseSql = "SELECT e.* FROM experiment e WHERE 1=1";
        StringBuilder sql = new StringBuilder(baseSql);

        // 应用数据权限过滤
        String dataFilter = buildDataFilter();
        sql.append(" AND ").append(dataFilter);

        // 项目过滤
        if (projectId != null) {
            sql.append(" AND e.project_id = :projectId");
        }

        sql.append(" ORDER BY e.created_at DESC LIMIT :offset, :limit");

        Query query = entityManager.createNativeQuery(sql.toString(), Experiment.class);
        query.setParameter("offset", (page - 1) * size);
        query.setParameter("limit", size);

        if (projectId != null) {
            query.setParameter("projectId", projectId);
        }

        return query.getResultList();
    }

    /**
     * 构建数据权限过滤条件
     */
    private String buildDataFilter() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof SysUser user)) {
            return "1=0"; // 未认证用户无法访问
        }

        // ADMIN 拥有全部权限
        if (user.getRoleCodes().contains("ADMIN")) {
            return "1=1";
        }

        String dataScope = user.getDataScope();

        return switch (dataScope) {
            case "all" -> "1=1";
            case "dept" -> String.format(
                "e.dept_id IN (SELECT dept_id FROM sys_user_dept WHERE user_id = %d)", user.getId());
            case "lab" -> String.format(
                "e.lab_id IN (SELECT lab_id FROM sys_user_lab WHERE user_id = %d)", user.getId());
            case "own" -> String.format("e.created_by = %d", user.getId());
            default -> String.format("e.created_by = %d", user.getId());
        };
    }

    /**
     * 计算符合条件且有权限访问的记录总数
     */
    public long countAccessibleExperiments(Long projectId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM experiment e WHERE 1=1");
        String dataFilter = buildDataFilter();
        sql.append(" AND ").append(dataFilter);

        if (projectId != null) {
            sql.append(" AND e.project_id = :projectId");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        if (projectId != null) {
            query.setParameter("projectId", projectId);
        }

        return ((Number) query.getSingleResult()).longValue();
    }
}