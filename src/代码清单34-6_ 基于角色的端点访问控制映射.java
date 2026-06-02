package com.example.monitoring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.Set;

@Configuration
public class RoleBasedEndpointConfig {

    @Bean
    public Map<String, Set<String>> endpointRoleMapping() {
        return Map.of(
            "health", Set.of("HEALTH_VIEWER", "ADMIN"),
            "info", Set.of("ADMIN"),
            "metrics", Set.of("METRICS_VIEWER", "ADMIN"),
            "env", Set.of("ADMIN"),
            "beans", Set.of("ADMIN"),
            "threaddump", Set.of("ADMIN"),
            "heapdump", Set.of("ADMIN")
        );
    }
}