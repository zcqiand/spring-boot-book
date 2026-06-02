package com.xrtech.chapter11.service;

import com.xrtech.chapter11.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl implements UserService {

    private final Map<Long, UserResponse> userStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1L);

    public UserServiceImpl() {
        // 初始化测试数据
        UserResponse user1 = new UserResponse();
        user1.setId(1L);
        user1.setUsername("alice");
        user1.setEmail("alice@example.com");
        user1.setFullName("Alice Smith");
        user1.setCreatedAt(LocalDateTime.now().toString());
        userStore.put(1L, user1);

        UserResponse user2 = new UserResponse();
        user2.setId(2L);
        user2.setUsername("bob");
        user2.setEmail("bob@example.com");
        user2.setFullName("Bob Johnson");
        user2.setCreatedAt(LocalDateTime.now().toString());
        userStore.put(2L, user2);
    }

    @Override
    public List<UserResponse> findAll() {
        return new ArrayList<>(userStore.values());
    }

    @Override
    public UserResponse findById(Long id) {
        UserResponse user = userStore.get(id);
        if (user == null) {
            throw new UserNotFoundException("用户不存在: " + id);
        }
        return user;
    }

    @Override
    public UserResponse create(UserCreateRequest request) {
        UserResponse user = new UserResponse();
        user.setId(idCounter.getAndIncrement());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now().toString());
        userStore.put(user.getId(), user);
        return user;
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        UserResponse user = findById(id);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUpdatedAt(LocalDateTime.now().toString());
        return user;
    }

    @Override
    public UserResponse patch(Long id, UserPatchRequest request) {
        UserResponse user = findById(id);
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        user.setUpdatedAt(LocalDateTime.now().toString());
        return user;
    }

    @Override
    public void delete(Long id) {
        if (userStore.remove(id) == null) {
            throw new UserNotFoundException("用户不存在: " + id);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}