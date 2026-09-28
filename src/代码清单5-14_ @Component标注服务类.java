package com.example.demo.service;

import org.springframework.stereotype.Component;

/**
 * 订单服务接口
 */
public interface OrderService {
    String createOrder(String productName, int quantity);
}

/**
 * 使用@Component将实现类注册为Spring Bean
 * 组件名称默认为类名首字母小写（orderServiceImpl）
 */
@Component
public class OrderServiceImpl implements OrderService {

    @Override
    public String createOrder(String productName, int quantity) {
        return String.format("订单已创建：%s x %d 件", productName, quantity);
    }
}