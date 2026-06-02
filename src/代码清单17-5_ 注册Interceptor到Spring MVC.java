package com.xrtech.web.config;

import com.xrtech.web.interceptor.AuthorizationInterceptor;
import com.xrtech.web.interceptor.UserContextInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * 注册所有HandlerInterceptor
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserContextInterceptor userContextInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;

    public WebMvcConfig(UserContextInterceptor userContextInterceptor,
                       AuthorizationInterceptor authorizationInterceptor) {
        this.userContextInterceptor = userContextInterceptor;
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 用户上下文Interceptor - 应用于所有请求
        registry.addInterceptor(userContextInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/api/auth/**",
                    "/api/public/**",
                    "/error"
                );

        // 权限校验Interceptor - 应用于所有API请求
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                    "/api/auth/**",
                    "/api/public/**"
                );
    }
}