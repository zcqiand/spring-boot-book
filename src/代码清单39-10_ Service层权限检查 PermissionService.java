package com.lab.security.service;

import com.lab.security.entity.SysUser;
import com.lab.security.holder.LabContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    public boolean hasPermission(SysUser user, String permission) {
        return user.getRoles().stream()
            .flatMap(role -> role.getAllPermCodes().stream())
            .anyMatch(perm -> perm.equals(permission));
    }

    public boolean hasDataPermission(SysUser user, String targetType, Long targetId, String permission) {
        if (!hasPermission(user, permission)) return false;

        LabContextHolder.DataPermissionContext ctx = LabContextHolder.getContext();
        if (ctx == null) return true;

        return switch (ctx.dataScope()) {
            case "all" -> true;
            case "own" -> checkOwnership(ctx.userId(), targetType, targetId);
            case "lab" -> checkLabAccess(ctx.userId(), targetId);
            default -> false;
        };
    }

    private boolean checkOwnership(Long userId, String targetType, Long targetId) {
        return true;
    }

    private boolean checkLabAccess(Long userId, Long targetId) {
        return true;
    }
}