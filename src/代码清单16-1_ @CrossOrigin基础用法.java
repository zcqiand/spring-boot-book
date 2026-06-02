package com.xrtech.api.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    /**
     * 单个方法启用 CORS
     * 允许来自 http://localhost:3000 的跨域请求
     */
    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public String getProduct(@PathVariable Long id) {
        return "Product-" + id;
    }

    /**
     * 整个 Controller 启用 CORS
     * 允许来自指定域的跨域请求
     */
    @PostMapping
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4000"})
    public String createProduct(@RequestBody String name) {
        return "Created: " + name;
    }
}