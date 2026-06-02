package com.xrtech.api.controller;

import com.xrtech.api.dto.BookCreateRequest;
import com.xrtech.api.dto.BookResponse;
import com.xrtech.api.dto.BookUpdateRequest;
import com.xrtech.api.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书管理REST控制器
 * 演示标准的RESTful API设计
 */
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * 查询所有图书
     * GET /api/v1/books
     */
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    /**
     * 根据ID查询图书
     * GET /api/v1/books/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    /**
     * 创建新图书
     * POST /api/v1/books
     * 返回201 Created和Location头
     */
    @PostMapping
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody BookCreateRequest request) {
        BookResponse created = bookService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/books/" + created.getId())
                .body(created);
    }

    /**
     * 完整更新图书
     * PUT /api/v1/books/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    /**
     * 部分更新图书
     * PATCH /api/v1/books/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<BookResponse> patchBook(
            @PathVariable Long id,
            @RequestBody BookUpdateRequest request) {
        return ResponseEntity.ok(bookService.patch(id, request));
    }

    /**
     * 删除图书
     * DELETE /api/v1/books/{id}
     * 返回204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}