package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付服务——使用构造器注入
 */
@Service
public class PaymentService {

    private final OrderService orderService;
    private final InventoryService inventoryService;

    /**
     * 构造器注入——Spring推荐方式
     * required=false 表示依赖可选
     */
    @Autowired
    public PaymentService(OrderService orderService, InventoryService inventoryService) {
        this.orderService = orderService;
        this.inventoryService = inventoryService;
    }

    public String processPayment(String orderId, double amount) {
        return String.format("支付成功：订单%s，金额%.2f", orderId, amount);
    }
}