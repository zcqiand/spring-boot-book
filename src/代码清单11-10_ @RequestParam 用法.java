package com.xrtech.chapter11.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    /**
     * 必需参数
     * URL: GET /api/search?keyword=Spring
     */
    @GetMapping
    public String search(@RequestParam String keyword) {
        return "搜索关键词: " + keyword;
    }

    /**
     * 可选参数 + 默认值
     * URL: GET /api/search?keyword=Java&page=2
     */
    @GetMapping("/page")
    public String searchWithPage(
            @RequestParam String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        return "搜索: " + keyword + ", 第" + page + "页";
    }

    /**
     * 指定参数名称（参数名与变量名不同）
     * URL: GET /api/search?q=SpringBoot
     */
    @GetMapping("/custom")
    public String searchCustom(@RequestParam("q") String query) {
        return "查询: " + query;
    }

    /**
     * 多个参数值（数组）
     * URL: GET /api/search/tags?tags=java&tags=spring&tags=boot
     */
    @GetMapping("/tags")
    public String searchByTags(@RequestParam String[] tags) {
        return "标签查询: " + String.join(", ", tags);
    }
}