package com.example.demo.config;

import com.example.demo.service.OrderService;
import com.example.demo.service.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Java配置类，等价于传统XML配置文件
 */
@Configuration
public class AppConfig {

    /**
     * 使用@Bean声明Bean
     * 方法名即为Bean名称（orderService）
     * 可以显式指定Bean名称：@Bean("myOrderService")
     */
    @Bean
    public OrderService orderService() {
        // 手动创建Bean实例，可在其中加入复杂逻辑
        return new OrderServiceImpl();
    }

    /**
     * 通过@Bean注入依赖
     * Spring会自动注入已存在的Bean
     */
    @Bean
    public OrderManager orderManager(OrderService orderService) {
        return new OrderManager(orderService);
    }
}