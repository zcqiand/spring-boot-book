package com.example.monitoring.health;

import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicatorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class CompositeHealthConfig {

    @Bean
    public CompositeHealthIndicator compositeHealthIndicator(
            HealthIndicatorRegistry registry) {

        Map<String, HealthIndicator> indicators = new LinkedHashMap<>();
        indicators.put("database", registry.get("databaseHealth"));
        indicators.put("business", registry.get("businessHealth"));

        return new CompositeHealthIndicator(registry, indicators);
    }
}