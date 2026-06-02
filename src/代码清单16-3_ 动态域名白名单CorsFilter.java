package com.xrtech.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.util.Set;

/**
 * 动态域名白名单 CORS 过滤器
 * 支持从配置中心或数据库动态加载白名单
 */
@Component
@Order(1)
public class DynamicCorsFilter implements Filter {

    // 模拟动态白名单（实际可从数据库或配置中心加载）
    private volatile Set<String> allowedOrigins = Set.of(
            "http://localhost:3000",
            "http://localhost:4000",
            "https://app.example.com",
            "https://admin.example.com"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String origin = httpRequest.getHeader("Origin");

        // 检查 origin 是否在白名单中
        if (isOriginAllowed(origin)) {
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Max-Age", "3600");
        }

        // 处理预检请求
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isOriginAllowed(String origin) {
        if (origin == null) {
            return false;
        }
        return allowedOrigins.contains(origin);
    }
}