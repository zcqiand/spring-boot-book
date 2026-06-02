package com.example.dal.service;

import com.example.dal.entity.User;
import com.example.dal.entity.Order;
import com.example.dal.document.Comment;
import com.example.dal.repository.jpa.UserRepository;
import com.example.dal.repository.jpa.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UnifiedDataServiceTest {

    @Autowired
    private UnifiedDataService unifiedDataService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testCreateUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("$2a$10$xxx");
        user.setEmail("test@example.com");
        User created = unifiedDataService.createUser(user);
        assertNotNull(created.getId());
        assertNotNull(created.getCreatedAt());
    }

    @Test
    void testGetUserByIdWithCache() {
        User user = new User();
        user.setUsername("cacheuser");
        user.setPassword("$2a$10$xxx");
        User created = unifiedDataService.createUser(user);
        User fetched1 = unifiedDataService.getUserById(created.getId());
        User fetched2 = unifiedDataService.getUserById(created.getId());
        assertEquals(fetched1.getId(), fetched2.getId());
    }

    @Test
    void testCreateOrder() {
        User user = new User();
        user.setUsername("orderuser");
        user.setPassword("$2a$10$xxx");
        User createdUser = unifiedDataService.createUser(user);
        Order order = new Order();
        order.setUserId(createdUser.getId());
        order.setAmount(new BigDecimal("99.99"));
        order.setStatus("PENDING");
        Order created = unifiedDataService.createOrder(order);
        assertNotNull(created.getOrderNo());
        assertEquals("PENDING", created.getStatus());
    }

    @Test
    void testGetUserOrders() {
        User user = new User();
        user.setUsername("listuser");
        user.setPassword("$2a$10$xxx");
        User createdUser = unifiedDataService.createUser(user);
        for (int i = 0; i < 3; i++) {
            Order order = new Order();
            order.setUserId(createdUser.getId());
            order.setAmount(new BigDecimal(String.valueOf(i * 10)));
            order.setStatus("PENDING");
            unifiedDataService.createOrder(order);
        }
        List<Order> orders = unifiedDataService.getUserOrders(createdUser.getId());
        assertEquals(3, orders.size());
    }

    @Test
    void testUpdateOrderStatus() {
        User user = new User();
        user.setUsername("statususer");
        user.setPassword("$2a$10$xxx");
        User createdUser = unifiedDataService.createUser(user);
        Order order = new Order();
        order.setUserId(createdUser.getId());
        order.setAmount(new BigDecimal("50.00"));
        order.setStatus("PENDING");
        Order created = unifiedDataService.createOrder(order);
        Order updated = unifiedDataService.updateOrderStatus(created.getOrderNo(), "PAID");
        assertEquals("PAID", updated.getStatus());
    }

    @Test
    void testAddComment() {
        User user = new User();
        user.setUsername("commentuser");
        user.setPassword("$2a$10$xxx");
        User createdUser = unifiedDataService.createUser(user);
        Comment comment = new Comment();
        comment.setUserId(createdUser.getId());
        comment.setUsername(createdUser.getUsername());
        comment.setTargetType("PRODUCT");
        comment.setTargetId("PROD-001");
        comment.setContent("Great product!");
        comment.setRating(5);
        Comment created = unifiedDataService.addComment(comment);
        assertNotNull(created.getId());
    }

    @Test
    void testGetUserStatistics() {
        User user = new User();
        user.setUsername("statsuser");
        user.setPassword("$2a$10$xxx");
        user.setEmail("stats@example.com");
        User createdUser = unifiedDataService.createUser(user);
        var stats = unifiedDataService.getUserStatistics(createdUser.getId());
        assertEquals(createdUser.getId(), stats.get("userId"));
        assertEquals("statsuser", stats.get("username"));
    }
}