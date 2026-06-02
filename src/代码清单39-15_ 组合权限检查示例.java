package com.lab.security.service;

import com.lab.security.context.LabContextHolder;
import com.lab.security.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

/**
 * 组合权限检查服务
 * 演示如何同时检查功能权限和数据权限
 */
@Service
@Slf4j
public class CombinedPermissionService {

    /**
     * 检查用户是否有权删除实验记录
     * 功能权限：必须有 experiment:delete 权限
     * 数据权限：只能删除自己负责项目的数据
     */
    public void checkDeletePermission(SysUser user, Experiment experiment) {
        // 步骤1：检查功能权限
        boolean hasFunctionPermission = user.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("system:experiment:delete"));

        if (!hasFunctionPermission) {
            throw new AccessDeniedException("您没有删除实验记录的权限");
        }

        // 步骤2：检查数据权限
        boolean hasDataPermission = checkDataPermission(user, experiment);

        if (!hasDataPermission) {
            throw new AccessDeniedException("您无权删除此实验记录");
        }

        log.info("权限检查通过 - 用户: {}, 实验ID: {}", user.getUsername(), experiment.getId());
    }

    /**
     * 检查数据权限
     * 根据用户角色决定可以操作的数据范围
     */
    private boolean checkDataPermission(SysUser user, Experiment experiment) {
        // ADMIN 可以操作所有数据
        if (user.getRoleCodes().contains("ADMIN")) {
            return true;
        }

        // MANAGER 可以操作本实验室的数据
        if (user.getRoleCodes().contains("MANAGER")) {
            return user.getLabs().stream()
                .anyMatch(lab -> lab.getId().equals(experiment.getLabId()));
        }

        // 其他角色只能操作自己的数据
        return experiment.getCreatedBy().equals(user.getId());
    }

    /**
     * 创建权限决策树
     * 用于复杂场景下的权限判断
     */
    public PermissionDecision decide(String action, SysUser user, Object resource) {
        return switch (action) {
            case "delete" -> decideDelete(user, resource);
            case "read" -> decideRead(user, resource);
            case "write" -> decideWrite(user, resource);
            default -> new PermissionDecision(false, "未知操作");
        };
    }

    private PermissionDecision decideDelete(SysUser user, Object resource) {
        if (resource instanceof Experiment exp) {
            if (!user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("system:experiment:delete"))) {
                return new PermissionDecision(false, "缺少 experiment:delete 权限");
            }
            if (!checkDataPermission(user, exp)) {
                return new PermissionDecision(false, "不在您的数据访问范围内");
            }
            return new PermissionDecision(true, "允许删除");
        }
        return new PermissionDecision(false, "不支持的资源类型");
    }

    private PermissionDecision decideRead(SysUser user, Object resource) {
        // 读取权限检查逻辑
        return new PermissionDecision(true, "允许读取");
    }

    private PermissionDecision decideWrite(SysUser user, Object resource) {
        // 写入权限检查逻辑
        return new PermissionDecision(true, "允许写入");
    }

    public record PermissionDecision(boolean allowed, String reason) {}
}