package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 库存服务
 */
@Service
public class InventoryService {

    @Autowired
    private OrderService orderService;  // 字段注入

    public String checkStock(String productName) {
        String orderResult = orderService.createOrder(productName, 1);
        return "库存检查：" + productName + "，已触发：" + orderResult;
    }
}