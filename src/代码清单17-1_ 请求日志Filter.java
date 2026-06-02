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
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

/**
 * 请求日志记录Filter
 * 记录每个请求的URL、方法、状态码和处理时长
 */
@Component
@Order(1)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        // 包装请求和响应，以便后续读取内容
        ContentCachingRequestWrapper wrappedRequest =
            new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse =
            new ContentCachingResponseWrapper(response);

        Instant startTime = Instant.now();
        String requestId = request.getHeader("X-Request-Id");

        // 记录请求信息
        log.info("[{}] {} {} - Start",
                requestId,
                request.getMethod(),
                request.getRequestURI());

        try {
            // 执行后续Filter链
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // 计算处理时长
            Duration duration = Duration.between(startTime, Instant.now());
            int status = wrappedResponse.getStatus();

            // 记录响应信息
            log.info("[{}] {} {} - {} ({}ms)",
                    requestId,
                    request.getMethod(),
                    request.getRequestURI(),
                    status,
                    duration.toMillis());

            // 必须调用此方法，将缓存内容写回响应
            wrappedResponse.copyBodyToResponse();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 跳过静态资源请求，减少日志噪音
        String path = request.getRequestURI();
        return path.startsWith("/static") ||
               path.startsWith("/favicon") ||
               path.endsWith(".js") ||
               path.endsWith(".css") ||
               path.endsWith(".png") ||
               path.endsWith(".jpg");
    }
}