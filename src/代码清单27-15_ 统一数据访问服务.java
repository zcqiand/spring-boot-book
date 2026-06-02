package com.example.dal.service;

import com.example.dal.entity.User;
import com.example.dal.entity.Order;
import com.example.dal.document.OperationLog;
import com.example.dal.document.Comment;
import com.example.dal.repository.jpa.UserRepository;
import com.example.dal.repository.jpa.OrderRepository;
import com.example.dal.repository.mongo.OperationLogRepository;
import com.example.dal.repository.mongo.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UnifiedDataService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private MongoService mongoService;

    // ========== 用户管理 ==========

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("邮箱已被注册");
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        User saved = userRepository.save(user);
        // 写入缓存
        redisCacheService.cacheUser(saved.getId(), saved);
        // 记录日志
        mongoService.saveOperationLog(buildLog(saved.getId(), "CREATE_USER", "创建用户"));
        return saved;
    }

    public User getUserById(Long userId) {
        // 优先从缓存获取
        Object cached = redisCacheService.getCachedUser(userId);
        if (cached != null) {
            return (User) cached;
        }
        // 从数据库获取
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            redisCacheService.cacheUser(userId, user);
            return user;
        }
        return null;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public User updateUser(Long userId, User updated) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new NoSuchElementException("用户不存在");
        }
        User user = userOpt.get();
        if (updated.getEmail() != null) {
            user.setEmail(updated.getEmail());
        }
        if (updated.getPhone() != null) {
            user.setPhone(updated.getPhone());
        }
        if (updated.getStatus() != null) {
            user.setStatus(updated.getStatus());
        }
        user.setUpdatedAt(LocalDateTime.now());
        User saved = userRepository.save(user);
        // 更新缓存
        redisCacheService.evictUser(userId);
        redisCacheService.cacheUser(userId, saved);
        // 记录日志
        mongoService.saveOperationLog(buildLog(userId, "UPDATE_USER", "更新用户"));
        return saved;
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("用户不存在");
        }
        userRepository.deleteById(userId);
        // 清除缓存
        redisCacheService.evictUser(userId);
        // 记录日志
        mongoService.saveOperationLog(buildLog(userId, "DELETE_USER", "删除用户"));
    }

    // ========== 订单管理 ==========

    @Transactional
    public Order createOrder(Order order) {
        if (!userRepository.existsById(order.getUserId())) {
            throw new NoSuchElementException("用户不存在");
        }
        order.setOrderNo(UUID.randomUUID().toString());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);
        // 写入缓存
        redisCacheService.cacheOrder(saved.getOrderNo(), saved);
        // 增加用户订单计数
        redisCacheService.incrementCounter("user:" + order.getUserId() + ":order_count");
        // 记录日志
        mongoService.saveOperationLog(buildLog(saved.getId(), "CREATE_ORDER", "创建订单"));
        return saved;
    }

    public Order getOrderByNo(String orderNo) {
        // 优先从缓存获取
        Object cached = redisCacheService.getCachedOrder(orderNo);
        if (cached != null) {
            return (Order) cached;
        }
        // 从数据库获取
        Optional<Order> orderOpt = orderRepository.findByOrderNo(orderNo);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            redisCacheService.cacheOrder(orderNo, order);
            return order;
        }
        return null;
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Order updateOrderStatus(String orderNo, String status) {
        Optional<Order> orderOpt = orderRepository.findByOrderNo(orderNo);
        if (!orderOpt.isPresent()) {
            throw new NoSuchElementException("订单不存在");
        }
        Order order = orderOpt.get();
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);
        // 更新缓存
        redisCacheService.evictOrder(orderNo);
        redisCacheService.cacheOrder(orderNo, saved);
        // 记录日志
        mongoService.saveOperationLog(buildLog(saved.getId(), "UPDATE_ORDER_STATUS", "更新订单状态"));
        return saved;
    }

    // ========== 评论管理 ==========

    @Transactional
    public Comment addComment(Comment comment) {
        // 检查用户是否存在
        if (!userRepository.existsById(comment.getUserId())) {
            throw new NoSuchElementException("用户不存在");
        }
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        Comment saved = commentRepository.save(comment);
        // 记录日志
        mongoService.saveOperationLog(buildLog(saved.getId(), "ADD_COMMENT", "添加评论"));
        return saved;
    }

    public List<Comment> getProductComments(String productId) {
        return commentRepository.findByTargetTypeAndTargetId("PRODUCT", productId);
    }

    public List<Comment> getProductCommentsSorted(String productId, String sortBy) {
        List<Comment> comments = commentRepository.findByTargetTypeAndTargetId("PRODUCT", productId);
        if ("rating".equals(sortBy)) {
            return comments.stream()
                .sorted(Comparator.comparing(Comment::getRating).reversed())
                .collect(Collectors.toList());
        } else if ("latest".equals(sortBy)) {
            return comments.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .collect(Collectors.toList());
        }
        return comments;
    }

    @Transactional
    public void likeComment(String commentId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            comment.setLikeCount(comment.getLikeCount() + 1);
            commentRepository.save(comment);
            // 记录日志
            mongoService.saveOperationLog(buildLog(comment.getId(), "LIKE_COMMENT", "点赞评论"));
        }
    }

    // ========== 日志查询 ==========

    public List<OperationLog> getUserOperationLogs(Long userId) {
        return mongoService.getUserOperationLogs(userId);
    }

    public List<OperationLog> getRecentOperations(int limit) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "createdAt")).limit(limit);
        return mongoTemplate.find(query, OperationLog.class);
    }

    // ========== 辅助方法 ==========

    private OperationLog buildLog(Long targetId, String operationType, String desc) {
        OperationLog log = new OperationLog();
        log.setOperationType(operationType);
        log.setOperationDesc(desc);
        log.setTargetType("USER");
        log.setTargetId(String.valueOf(targetId));
        log.setCreatedAt(LocalDateTime.now());
        return log;
    }

    // ========== 业务统计 ==========

    public Map<String, Object> getUserStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userId", userId);
        // 用户信息
        User user = getUserById(userId);
        stats.put("username", user != null ? user.getUsername() : null);
        // 订单数量
        Long orderCount = orderRepository.countByUserId(userId);
        stats.put("orderCount", orderCount);
        // 评论数量
        Long commentCount = mongoService.countTargetComments("USER", String.valueOf(userId));
        stats.put("commentCount", commentCount);
        // 操作日志数量
        List<OperationLog> logs = mongoService.getUserOperationLogs(userId);
        stats.put("operationCount", logs.size());
        return stats;
    }
}