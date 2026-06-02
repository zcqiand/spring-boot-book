package com.xrtech.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 认证Filter
 * 检查请求头中的Token，进行基础认证验证
 */
@Component
@Order(2)
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        // 放行登录和注册接口
        String path = request.getRequestURI();
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 获取Token
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("Missing or invalid Authorization header for {}", path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"success\":false,\"code\":401,\"message\":\"未提供认证Token\"}");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        // 验证Token（此处简化处理，实际应调用认证服务）
        if (!validateToken(token)) {
            log.warn("Invalid token for {}: {}", path, token.substring(0, 10));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"success\":false,\"code\":401,\"message\":\"Token无效或已过期\"}");
            return;
        }

        // Token验证通过，继续处理
        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/api/auth/login") ||
               path.startsWith("/api/auth/register") ||
               path.startsWith("/api/public");
    }

    private boolean validateToken(String token) {
        // 实际项目中应调用JWT验证服务或Redis验证
        // 此处简化处理：Token长度大于10即认为有效
        return token != null && token.length() > 10;
    }
}