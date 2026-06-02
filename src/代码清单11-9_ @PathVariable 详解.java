package com.xrtech.chapter11.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    /**
     * 单个路径变量
     * URL: GET /api/books/100
     */
    @GetMapping("/{bookId}")
    public String getBook(@PathVariable Long bookId) {
        return "图书ID: " + bookId;
    }

    /**
     * 多个路径变量
     * URL: GET /api/books/100/chapters/5
     */
    @GetMapping("/{bookId}/chapters/{chapterNum}")
    public String getChapter(
            @PathVariable Long bookId,
            @PathVariable Integer chapterNum) {
        return "图书" + bookId + "的第" + chapterNum + "章";
    }

    /**
     * 路径变量名称与参数名不同，需显式指定
     * URL: GET /api/books/isbn/978-7-111-12345-6
     */
    @GetMapping("/isbn/{isbn}")
    public String getBookByIsbn(@PathVariable("isbn") String isbnCode) {
        return "ISBN: " + isbnCode;
    }

    /**
     * 正则路径约束 - 仅匹配数字
     * URL: GET /api/books/numeric/12345
     * 不会匹配 /api/books/numeric/abc
     */
    @GetMapping("/numeric/{id:[0-9]+}")
    public String getBookNumeric(@PathVariable Long id) {
        return "数字ID: " + id;
    }

    /**
     * 正则路径约束 - 仅匹配字母
     * URL: GET /api/books/alpha/abc123
     * 不会匹配 /api/books/alpha/123
     */
    @GetMapping("/alpha/{code:[a-zA-Z]+}")
    public String getBookAlpha(@PathVariable String code) {
        return "字母代码: " + code;
    }
}