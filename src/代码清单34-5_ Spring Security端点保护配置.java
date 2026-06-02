package com.example.monitoring.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ActuatorSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // 健康检查端点允许匿名访问
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/health/**").permitAll()

                // info端点允许匿名访问
                .requestMatchers("/actuator/info").permitAll()

                // metrics端点需要认证
                .requestMatchers("/actuator/metrics/**").hasRole("METRICS_VIEWER")

                // 敏感端点需要管理员角色
                .requestMatchers("/actuator/env/**").hasRole("ADMIN")
                .requestMatchers("/actuator/beans/**").hasRole("ADMIN")
                .requestMatchers("/actuator/threaddump/**").hasRole("ADMIN")
                .requestMatchers("/actuator/heapdump/**").hasRole("ADMIN")

                // 所有actuator端点需要认证
                .requestMatchers("/actuator/**").authenticated()

                // 其他请求允许
                .anyRequest().permitAll()
            )
            .httpBasic(basic -> basic
                .realmName("Actuator API")
            )
            .csrf(csrf -> csrf
                // 禁用Actuator端点的CSRF保护
                .ignoringRequestMatchers("/actuator/**")
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN", "HEALTH_VIEWER", "METRICS_VIEWER")
                .build());

        manager.createUser(User.builder()
                .username("viewer")
                .password(passwordEncoder.encode("viewer123"))
                .roles("HEALTH_VIEWER", "METRICS_VIEWER")
                .build());

        return manager;
    }
}