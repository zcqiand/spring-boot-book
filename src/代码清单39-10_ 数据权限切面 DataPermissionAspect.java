package com.lab.manager.aspect;

import com.lab.security.entity.SysUser;
import com.lab.security.context.LabContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据权限切面
 * 在方法执行前注入数据范围过滤条件
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DataPermissionAspect {

    @Around("@annotation(dataPermission) || @within(dataPermission)")
    public Object around(ProceedingJoinPoint joinPoint, DataPermission dataPermission) throws Throwable {
        // 获取当前用户
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof SysUser user)) {
            return joinPoint.proceed();
        }

        // 获取数据范围
        String dataScope = user.getDataScope();
        String resourceType = dataPermission.resourceType();

        // 管理员拥有全部数据访问权限
        if (user.getRoleCodes().contains("ADMIN")) {
            dataScope = "all";
        }

        // 设置数据权限上下文
        DataScopeContext context = new DataScopeContext(user.getId(), dataScope, resourceType);
        LabContextHolder.setDataScope(context);

        try {
            log.debug("数据权限过滤生效 - 用户: {}, 范围: {}, 资源: {}",
                user.getUsername(), dataScope, resourceType);
            return joinPoint.proceed();
        } finally {
            LabContextHolder.clear();
        }
    }

    /**
     * 数据权限上下文
     */
    public record DataScopeContext(Long userId, String dataScope, String resourceType) {}
}