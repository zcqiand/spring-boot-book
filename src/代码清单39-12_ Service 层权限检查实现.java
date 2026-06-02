package com.lab.security.service;

import com.lab.security.context.LabContextHolder;
import com.lab.security.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    // 注入 Repository
    private final ExperimentRepository experimentRepository;
    private final DeviceRepository deviceRepository;

    /**
     * 检查用户是否具有指定权限
     */
    public boolean hasPermission(SysUser user, String permCode, String dataScope) {
        // 检查功能权限
        boolean hasFunctionPermission = user.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals(permCode));

        if (!hasFunctionPermission) {
            return false;
        }

        // 检查数据权限
        return hasDataScope(user, dataScope);
    }

    /**
     * 检查用户是否具有数据权限
     */
    public boolean hasDataScope(SysUser user, String requiredScope) {
        String userScope = user.getDataScope();
        return getScopePriority(userScope) >= getScopePriority(requiredScope);
    }

    /**
     * 检查用户是否能访问特定资源
     */
    public boolean hasDataPermission(SysUser user, String resourceType, Long resourceId, String permCode) {
        // ADMIN 可以访问所有资源
        if (user.getRoleCodes().contains("ADMIN")) {
            return true;
        }

        LabContextHolder.DataScopeContext context = LabContextHolder.getDataScope();
        if (context != null) {
            String dataScope = context.dataScope();
            return hasDataScope(user, dataScope);
        }

        // 默认只允许访问自己的数据
        return "own".equals(user.getDataScope());
    }

    /**
     * 生成数据权限过滤条件（用于 SQL 查询）
     */
    public String buildDataFilterSql(String resourceType) {
        LabContextHolder.DataScopeContext context = LabContextHolder.getDataScope();
        if (context == null) {
            return "1=1"; // 无过滤条件
        }

        return switch (context.dataScope()) {
            case "all" -> "1=1"; // 无限制
            case "dept" -> String.format("dept_id = (SELECT dept_id FROM sys_user WHERE id = %d)", context.userId());
            case "lab" -> String.format("lab_id IN (SELECT lab_id FROM sys_user_lab WHERE user_id = %d)", context.userId());
            case "own" -> String.format("created_by = %d", context.userId());
            default -> "1=1";
        };
    }

    private int getScopePriority(String scope) {
        return switch (scope) {
            case "all" -> 4;
            case "dept" -> 3;
            case "lab" -> 2;
            case "own" -> 1;
            default -> 0;
        };
    }
}