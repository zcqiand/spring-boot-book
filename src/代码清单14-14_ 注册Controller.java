package com.xrtech.api.controller;

import com.xrtech.api.request.RegisterRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping("/register")
    public Map<String, Object> register(
            @Validated @RequestBody RegisterRequest request) {

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "注册成功");
        result.put("data", Map.of(
            "username", request.getUsername(),
            "email", request.getEmail()
        ));
        return result;
    }
}