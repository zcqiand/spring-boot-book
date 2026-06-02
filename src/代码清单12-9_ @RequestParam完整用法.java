package com.xrtech.chapter12.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Query参数处理控制器
 * 展示@RequestParam的所有用法
 */
@RestController
@RequestMapping("/api/articles")
public class ArticleQueryController {

    /**
     * 必需参数 - 默认行为
     * GET /api/articles/search?keyword=Spring
     */
    @GetMapping("/search")
    public String search(@RequestParam String keyword) {
        return "搜索: " + keyword;
    }

    /**
     * 可选参数 + 默认值
     * GET /api/articles/list?page=2&size=20
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return "第" + page + "页，每页" + size + "条";
    }

    /**
     * 指定参数名（参数名与变量名不同）
     * GET /api/articles?q=Java
     */
    @GetMapping("/custom")
    public String customSearch(@RequestParam("q") String query) {
        return "查询: " + query;
    }

    /**
     * 多个同名参数（数组）
     * GET /api/articles/filter?tag=java&tag=spring&tag=boot
     */
    @GetMapping("/filter")
    public String filterByTags(@RequestParam List<String> tag) {
        return "标签过滤: " + String.join(", ", tag);
    }

    /**
     * 可选参数（required=false + 包装类型）
     * GET /api/articles/category?cat=tech
     * GET /api/articles/category （无参数）
     */
    @GetMapping("/category")
    public String category(@RequestParam(required = false) String cat) {
        return cat != null ? "分类: " + cat : "未指定分类";
    }
}