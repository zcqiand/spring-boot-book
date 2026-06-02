package com.xrtech.chapter11.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    /**
     * 指定HTTP方法和路径
     * 完整写法，等同于 @GetMapping("/list")
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listProducts() {
        return "产品列表";
    }

    /**
     * 消费类型：只接受JSON请求
     * 生产类型：返回JSON响应
     */
    @RequestMapping(
        value = "/create",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String createProduct() {
        return "{\"status\": \"created\"}";
    }

    /**
     * 按ID查询单个产品
     */
    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id) {
        return "产品ID: " + id;
    }

    /**
     * 批量查询，支持分页参数
     */
    @GetMapping("/batch")
    public String batchGet(
            @RequestParam("ids") String ids,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        return "批量查询: " + ids + ", 页码: " + page;
    }
}