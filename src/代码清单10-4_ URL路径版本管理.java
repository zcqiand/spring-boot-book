package com.xrtech.api.controller;

/**
 * 策略一：URL路径版本管理（最常用）
 * 清晰度高，可缓存，可书签
 * 适用场景：需要重大版本升级时
 */
@RestController
@RequestMapping("/api/v1/books")
public class BookControllerV1 {

    @GetMapping
    public ResponseEntity<String> listBooksV1() {
        return ResponseEntity.ok("""
            {"version": "v1", "books": [...], "format": "简化版"}
            """);
    }
}

/**
 * 策略二：Query参数版本管理
 * 不改变URL结构，但客户端传递不便，影响缓存
 * 适用场景：临时性版本测试
 */
@RestController
@RequestMapping("/api/books")
public class BookControllerQueryVersion {

    @GetMapping
    public ResponseEntity<String> listBooks(
            @RequestParam(value = "version", defaultValue = "1") int version) {
        return switch (version) {
            case 1 -> ResponseEntity.ok("返回v1格式数据");
            case 2 -> ResponseEntity.ok("返回v2格式数据");
            default -> ResponseEntity.badRequest().body("Unsupported version");
        };
    }
}

/**
 * 策略三：Header版本管理
 * URL整洁，需客户端传递额外Header
 * 适用场景：API消费者固定的企业内部服务
 */
@RestController
@RequestMapping("/api/books")
public class BookControllerHeaderVersion {

    @GetMapping
    public ResponseEntity<String> listBooks(
            @RequestHeader(value = "X-API-Version", defaultValue = "1") int version) {
        return switch (version) {
            case 1 -> ResponseEntity.ok("返回v1格式数据（通过Header指定）");
            case 2 -> ResponseEntity.ok("返回v2格式数据（通过Header指定）");
            default -> ResponseEntity.badRequest().body("Unsupported version");
        };
    }
}

/**
 * 策略四：Content Negotiation（符合REST设计）
 * 通过Accept头协商响应格式
 * 适用场景：需要细粒度版本控制
 */
@RestController
@RequestMapping("/api/books")
public class BookControllerContentNegotiation {

    @GetMapping(produces = "application/vnd.xrtech.v1+json")
    public ResponseEntity<String> listBooksV1() {
        return ResponseEntity.ok("返回v1格式数据");
    }

    @GetMapping(produces = "application/vnd.xrtech.v2+json")
    public ResponseEntity<String> listBooksV2() {
        return ResponseEntity.ok("返回v2格式数据");
    }
}