package com.xrtech.chapter12.controller;

import com.xrtech.chapter12.dto.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * JSON请求体处理控制器
 * 展示@RequestBody的完整用法
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final Map<Long, Product> productStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    public ProductController() {
        // 初始化测试数据
        Product p = new Product();
        p.setId(1L);
        p.setName("Spring Boot实战");
        p.setPrice(79.99);
        p.setStock(100);
        p.setCategory("技术书");
        productStore.put(1L, p);
    }

    /**
     * 创建产品 - JSON请求体
     * POST /api/products
     * Content-Type: application/json
     * Body: {"name": "xxx", "price": 99.99, ...}
     */
    @PostMapping
    public Product createProduct(@RequestBody CreateProductRequest request) {
        Product product = new Product();
        product.setId(idGenerator.getAndIncrement());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        product.setTags(request.getTags());
        product.setCreateTime(LocalDate.now().toString());
        productStore.put(product.getId(), product);
        return product;
    }

    /**
     * 批量创建产品
     * POST /api/products/batch
     * Body: [{"name": "xxx", ...}, {"name": "yyy", ...}]
     */
    @PostMapping("/batch")
    public List<Product> batchCreate(@RequestBody List<CreateProductRequest> requests) {
        List<Product> results = new ArrayList<>();
        for (CreateProductRequest request : requests) {
            Product product = new Product();
            product.setId(idGenerator.getAndIncrement());
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setStock(request.getStock());
            product.setCategory(request.getCategory());
            product.setCreateTime(LocalDate.now().toString());
            productStore.put(product.getId(), product);
            results.add(product);
        }
        return results;
    }

    /**
     * 嵌套JSON对象
     * Body: {"name": "xxx", "spec": {"weight": "500g", "size": "10cm"}}
     */
    @PostMapping("/with-spec")
    public Product createWithSpec(@RequestBody CreateProductWithSpecRequest request) {
        Product product = new Product();
        product.setId(idGenerator.getAndIncrement());
        product.setName(request.getName());
        if (request.getSpec() != null) {
            product.setSpec(request.getSpec());
        }
        product.setCreateTime(LocalDate.now().toString());
        productStore.put(product.getId(), product);
        return product;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productStore.get(id);
    }
}