package com.example.monitoring;

import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicatorRegistry;
import org.springframework.boot.actuate.health.OrderedHealthAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 健康检查聚合配置
 * 将多个健康检查指示器组合成一个统一的健康检查响应
 */
@Configuration
public class HealthAggregatorConfig {

    @Bean
    public HealthAggregator healthAggregator() {
        return new OrderedHealthAggregator();
    }

    @Bean
    public CompositeHealthIndicator compositeHealthIndicator(
            HealthIndicatorRegistry registry,
            HealthAggregator healthAggregator) {
        return new CompositeHealthIndicator(healthAggregator, registry);
    }
}