package com.example.dal.controller;

import com.example.dal.entity.User;
import com.example.dal.entity.Order;
import com.example.dal.document.Comment;
import com.example.dal.service.UnifiedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/data")
public class DataAccessController {

    @Autowired
    private UnifiedDataService unifiedDataService;

    // ---------- 用户管理 ----------

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(unifiedDataService.createUser(user));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = unifiedDataService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = unifiedDataService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(unifiedDataService.updateUser(id, user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        unifiedDataService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // ---------- 订单管理 ----------

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(unifiedDataService.createOrder(order));
    }

    @GetMapping("/orders/{orderNo}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderNo) {
        Order order = unifiedDataService.getOrderByNo(orderNo);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(unifiedDataService.getUserOrders(userId));
    }

    @PatchMapping("/orders/{orderNo}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable String orderNo,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return ResponseEntity.ok(unifiedDataService.updateOrderStatus(orderNo, status));
    }

    // ---------- 评论管理 ----------

    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(unifiedDataService.addComment(comment));
    }

    @GetMapping("/products/{productId}/comments")
    public ResponseEntity<List<Comment>> getProductComments(
            @PathVariable String productId,
            @RequestParam(defaultValue = "latest") String sortBy) {
        return ResponseEntity.ok(
            unifiedDataService.getProductCommentsSorted(productId, sortBy));
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable String commentId) {
        unifiedDataService.likeComment(commentId);
        return ResponseEntity.ok().build();
    }

    // ---------- 统计 ----------

    @GetMapping("/users/{userId}/statistics")
    public ResponseEntity<Map<String, Object>> getUserStatistics(@PathVariable Long userId) {
        return ResponseEntity.ok(unifiedDataService.getUserStatistics(userId));
    }
}