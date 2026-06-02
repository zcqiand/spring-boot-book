package com.xrtech.chapter11.controller;

import org.springframework.web.bind.annotation.RestController;

/**
 * @RestController = @Controller + @ResponseBody
 *
 * 所有返回方法直接写入HTTP响应体（序列化JSON），
 * 无需再添加@ResponseBody注解
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public String listUsers() {
        return "返回用户列表JSON（自动序列化）";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id) {
        return "{\"id\": " + id + ", \"name\": \"示例用户\"}";
    }
}