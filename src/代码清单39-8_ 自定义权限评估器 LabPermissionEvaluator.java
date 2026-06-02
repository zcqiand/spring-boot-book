package com.lab.security;

import com.lab.security.entity.SysUser;
import com.lab.security.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class LabPermissionEvaluator implements PermissionEvaluator {

    private final PermissionService permissionService;

    @Override
    public boolean hasPermission(Authentication auth, Object target, Object permission) {
        if (auth == null || target == null || permission == null) return false;
        SysUser user = (SysUser) auth.getPrincipal();
        return permissionService.hasPermission(user, (String) permission);
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable id, String type, Object permission) {
        if (auth == null || id == null || type == null || permission == null) return false;
        SysUser user = (SysUser) auth.getPrincipal();
        return permissionService.hasDataPermission(user, type, (Long) id, (String) permission);
    }
}