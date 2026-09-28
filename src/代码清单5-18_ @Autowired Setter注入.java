package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通知服务——使用Setter注入
 */
@Service
public class NotificationService {

    private OrderService orderService;

    /**
     * Setter注入——可选依赖场景
     */
    @Autowired(required = false)
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public String sendNotification(String message) {
        if (orderService == null) {
            return "通知（无订单服务）：" + message;
        }
        return "通知：" + message + "，关联订单：" + orderService.createOrder("默认商品", 1);
    }
}