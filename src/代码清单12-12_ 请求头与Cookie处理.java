package com.xrtech.chapter12.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求头与Cookie处理控制器
 */
@RestController
@RequestMapping("/api/client")
public class ClientInfoController {

    /**
     * 获取客户端信息
     * GET /api/client/info
     * Header: Authorization: Bearer xxx
     * Header: Accept-Language: zh-CN,zh;q=0.9
     */
    @GetMapping("/info")
    public Map<String, String> getClientInfo(
            @RequestHeader("User-Agent") String userAgent,
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String acceptLang,
            @RequestHeader(value = "Authorization", required = false) String auth) {

        Map<String, String> info = new HashMap<>();
        info.put("userAgent", userAgent);
        info.put("acceptLanguage", acceptLang);
        info.put("authenticated", auth != null ? "true" : "false");
        return info;
    }

    /**
     * 从Cookie获取会话信息
     * GET /api/client/cart
     * Cookie: cart_id=abc123; theme=dark
     */
    @GetMapping("/cart")
    public Map<String, String> getCartInfo(
            @CookieValue("cart_id") String cartId,
            @CookieValue(value = "theme", defaultValue = "light") String theme) {

        Map<String, String> cart = new HashMap<>();
        cart.put("cartId", cartId);
        cart.put("theme", theme);
        return cart;
    }

    /**
     * 获取所有请求头
     * 用于调试或日志记录
     */
    @GetMapping("/headers")
    public Map<String, String> getAllHeaders(@RequestHeader Map<String, String> headers) {
        // 返回所有请求头（可用于调试）
        return headers;
    }

    /**
     * 提取Bearer Token
     * Header: Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     */
    @GetMapping("/token")
    public Map<String, String> extractToken(
            @RequestHeader("Authorization") String authHeader) {

        Map<String, String> result = new HashMap<>();
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            result.put("token", authHeader.substring(7));
            result.put("valid", "true");
        } else {
            result.put("token", "");
            result.put("valid", "false");
        }
        return result;
    }
}