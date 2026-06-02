package com.xrtech.web.interceptor;

import com.xrtech.web.annotation.RequireRole;
import com.xrtech.web.context.UserContext;
import com.xrtech.web.context.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限校验Interceptor
 * 检查方法上的@RequireRole注解，进行基于角色的访问控制
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {

        // 只处理Controller方法
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 查找方法上的@RequireRole注解
        RequireRole methodRole = AnnotationUtils.findAnnotation(
            handlerMethod.getMethod(), RequireRole.class);

        // 如果方法上没有注解，查找类上的注解
        RequireRole classRole = null;
        if (methodRole == null) {
            classRole = AnnotationUtils.findAnnotation(
                handlerMethod.getBeanType(), RequireRole.class);
        }

        RequireRole requiredRole = methodRole != null ? methodRole : classRole;

        // 如果没有@RequireRole注解，放行
        if (requiredRole == null) {
            return true;
        }

        // 获取当前用户上下文
        UserContext userContext = UserContextHolder.getContext();
        if (userContext == null) {
            log.warn("权限校验失败：用户未登录");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"success\":false,\"code\":401,\"message\":\"请先登录\"}");
            return false;
        }

        // 检查用户角色是否满足要求
        String userRole = userContext.getRole();
        String[] allowedRoles = requiredRole.value();

        for (String role : allowedRoles) {
            if (role.equals(userRole)) {
                log.debug("权限校验通过: userRole={}, required={}", userRole, role);
                return true;
            }
        }

        log.warn("权限校验失败：用户角色{}不满足要求，需要{}",
                userRole, String.join(",", allowedRoles));
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(
            "{\"success\":false,\"code\":403,\"message\":\"权限不足\"}");
        return false;
    }
}