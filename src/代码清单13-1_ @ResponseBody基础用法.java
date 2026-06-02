package com.xrtech.api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserQueryController {

    // 直接返回对象，自动序列化为JSON
    @GetMapping("/{id}")
    public UserVO getUser(@PathVariable Long id) {
        return new UserVO(id, "alice@example.com", "Alice");
    }

    // 返回列表，同样自动序列化
    @GetMapping
    public List<UserVO> listUsers() {
        return List.of(
            new UserVO(1L, "alice@example.com", "Alice"),
            new UserVO(2L, "bob@example.com", "Bob")
        );
    }

    // 返回Map，自动序列化为JSON对象
    @GetMapping("/count")
    public java.util.Map<String, Object> getCount() {
        return java.util.Map.of(
            "total", 100,
            "active", 85
        );
    }
}

record UserVO(Long id, String email, String name) {}