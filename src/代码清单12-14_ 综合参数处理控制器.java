package com.xrtech.chapter12.controller;

import com.xrtech.chapter12.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 综合参数处理控制器
 * 展示所有参数类型的综合使用
 *
 * API端点：
 * GET  /api/orders                    - Query参数
 * POST /api/orders                    - JSON请求体
 * GET  /api/orders/{id}               - 路径参数
 * GET  /api/orders/{id}/status        - 路径+Query
 * POST /api/orders/{id}/process       - 路径+JSON+Header
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    /**
     * 查询订单列表 - Query参数
     * GET /api/orders?status=pending&page=1&size=20
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {

        // 返回模拟数据
        return ResponseEntity.ok(List.of());
    }

    /**
     * 创建订单 - JSON请求体
     * POST /api/orders
     * Content-Type: application/json
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        OrderResponse response = new OrderResponse();
        response.setId(1001L);
        response.setOrderNo("ORD" + System.currentTimeMillis());
        response.setCustomerName(request.getCustomerName());
        response.setTotalAmount(request.getTotalAmount());
        response.setStatus("created");
        response.setCreateTime(LocalDateTime.now().toString());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/orders/" + response.getId())
                .body(response);
    }

    /**
     * 获取订单详情 - 路径参数
     * GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse response = new OrderResponse();
        response.setId(id);
        response.setOrderNo("ORD" + id);
        response.setCustomerName("示例客户");
        response.setTotalAmount(99.99);
        response.setStatus("pending");
        response.setCreateTime(LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    /**
     * 查询订单状态变化记录 - 路径+Query
     * GET /api/orders/{id}/history?from=2024-01-01
     */
    @GetMapping("/{id}/history")
    public ResponseEntity<List<OrderStatusHistory>> getOrderHistory(
            @PathVariable Long id,
            @RequestParam(required = false) String from) {

        // 返回模拟数据
        return ResponseEntity.ok(List.of());
    }

    /**
     * 处理订单 - 多种参数组合
     * POST /api/orders/{id}/process
     * Header: X-Operator: admin
     * JSON Body: {"action": "approve", "comment": "ok"}
     */
    @PostMapping("/{id}/process")
    public ResponseEntity<OrderResponse> processOrder(
            @PathVariable Long id,
            @RequestHeader("X-Operator") String operator,
            @Valid @RequestBody ProcessOrderRequest request) {

        OrderResponse response = new OrderResponse();
        response.setId(id);
        response.setStatus(request.getAction().equals("approve") ? "approved" : "rejected");
        response.setUpdateTime(LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }
}

/**
 * 订单创建请求DTO
 */
class CreateOrderRequest {
    @NotBlank(message = "客户名称不能为空")
    private String customerName;

    @NotBlank(message = "联系方式不能为空")
    private String contactPhone;

    @NotNull(message = "总金额不能为空")
    private Double totalAmount;

    @NotEmpty(message = "订单项不能为空")
    private List<OrderItem> items;

    private String remark;

    // getter和setter
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

/**
 * 订单项
 */
class OrderItem {
    @NotBlank(message = "商品名称不能为空")
    private String productName;

    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量至少为1")
    private Integer quantity;

    @NotNull(message = "单价不能为空")
    private Double price;

    // getter和setter
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}

/**
 * 订单响应DTO
 */
class OrderResponse {
    private Long id;
    private String orderNo;
    private String customerName;
    private Double totalAmount;
    private String status;
    private String createTime;
    private String updateTime;

    // getter和setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}

/**
 * 订单状态历史
 */
class OrderStatusHistory {
    private String status;
    private String operator;
    private String comment;
    private String timestamp;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}

/**
 * 订单处理请求
 */
class ProcessOrderRequest {
    @NotBlank(message = "操作类型不能为空")
    private String action;  // approve, reject

    private String comment;

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}