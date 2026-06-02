package com.xrtech.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot启动类
 *
 * @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
 * - @Configuration：标记为配置类
 * - @EnableAutoConfiguration：启用Spring Boot自动配置
 * - @ComponentScan：扫描当前包及子包的组件（@Service, @Repository, @Controller等）
 */
@SpringBootApplication
public class JpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }
}