package com.example.demo.config;

import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheAutoConfiguration {

    // 条件一：只有当classpath中存在Redis相关类时，才注册RedisCacheManager
    // 如果没有引入spring-boot-starter-data-redis，这个Bean就不会被注册
    // 这是最常用的条件注解之一，用于检测某个功能模块是否被引入
    // 注意：这里使用name参数指定类名，而不是直接使用Class对象
    // 这样可以避免ClassNotFoundException，即使该类不存在也不会导致应用启动失败
    // 这是一种防御性编程，确保在类不存在时应用仍然可以启动
    @Bean
    @ConditionalOnClass(name = "org.springframework.data.redis.cache.RedisCacheManager")
    public CacheManager redisCacheManager() {
        System.out.println(">>> [自动配置] 注册 RedisCacheManager");
        return new RedisCacheManager();
    }

    // 条件二：如果没有Redis，则注册简单的内存CacheManager作为默认实现
    // @ConditionalOnMissingBean保证：只有当容器中还没有CacheManager时才注册
    // 这个注解确保用户手动定义的Bean优先于自动配置
    // 如果用户已经定义了CacheManager类型的Bean，这个Bean就不会被注册
    // 这是Spring Boot"约定优于配置"原则的核心体现
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager simpleCacheManager() {
        System.out.println(">>> [自动配置] 注册 SimpleCacheManager（默认内存缓存）");
        return new SimpleCacheManager();
    }

    // 条件三：只有当application.yml中设置了 spring.cache.enabled=true 时才启用
    // matchIfMissing = false 表示如果配置项不存在，这个条件就不满足
    // matchIfMissing = true 则表示如果配置项不存在，条件默认满足
    // 这个设计允许你通过配置来控制是否启用某个功能
    // havingValue指定了必须匹配的值，只有完全相等条件才满足
    @Bean
    @ConditionalOnProperty(
        name = "spring.cache.enabled",
        havingValue = "true",
        matchIfMissing = false
    )
    public CacheManager conditionalCacheManager() {
        System.out.println(">>> [自动配置] 注册 ConditionalCacheManager（需要配置启用）");
        return new ConditionalCacheManager();
    }

    // 条件四：只有当应用是Web应用时才注册Web相关的缓存处理器
    // 这确保了自动配置不会在非Web环境中引入不必要的Bean
    // Spring Boot会检测当前应用类型（Servlet、Reactive或None）
    @Bean
    @ConditionalOnWebApplication
    public CacheManager webCacheManager() {
        System.out.println(">>> [自动配置] 注册 WebCacheManager（仅Web应用）");
        return new WebCacheManager();
    }
}