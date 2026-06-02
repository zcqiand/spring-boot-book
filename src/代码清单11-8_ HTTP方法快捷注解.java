package com.xrtech.chapter11.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    /**
     * GET /api/orders - 查询所有订单
     */
    @GetMapping
    public String getAllOrders() {
        return "返回所有订单列表";
    }

    /**
     * GET /api/orders/{orderId} - 根据ID查询订单
     */
    @GetMapping("/{orderId}")
    public String getOrderById(@PathVariable Long orderId) {
        return "订单详情: " + orderId;
    }

    /**
     * POST /api/orders - 创建新订单
     */
    @PostMapping
    public String createOrder(@RequestBody String orderData) {
        return "创建订单: " + orderData;
    }

    /**
     * PUT /api/orders/{orderId} - 完整更新订单
     */
    @PutMapping("/{orderId}")
    public String updateOrder(
            @PathVariable Long orderId,
            @RequestBody String orderData) {
        return "完整更新订单" + orderId + ": " + orderData;
    }

    /**
     * PATCH /api/orders/{orderId} - 部分更新订单
     */
    @PatchMapping("/{orderId}")
    public String patchOrder(
            @PathVariable Long orderId,
            @RequestBody String partialData) {
        return "部分更新订单" + orderId + ": " + partialData;
    }

    /**
     * DELETE /api/orders/{orderId} - 删除订单
     */
    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {
        return "删除订单: " + orderId;
    }
}