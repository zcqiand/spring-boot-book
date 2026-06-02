package com.xrtech.chapter11.controller;

import com.xrtech.chapter11.dto.CommentRequest;
import com.xrtech.chapter11.dto.CommentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论管理REST控制器
 *
 * API端点：
 * GET    /api/v1/articles/{articleId}/comments     - 查询评论
 * POST   /api/v1/articles/{articleId}/comments     - 发表评论
 * PUT    /api/v1/articles/{articleId}/comments/{commentId} - 修改评论
 * DELETE /api/v1/articles/{articleId}/comments/{commentId} - 删除评论
 */
@RestController
@RequestMapping("/api/v1/articles/{articleId}/comments")
public class CommentController {

    /**
     * 查询某篇文章的所有评论（支持分页）
     */
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long articleId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        // 实际从Service获取
        return ResponseEntity.ok(List.of());
    }

    /**
     * 发表评论
     */
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long articleId,
            @RequestBody CommentRequest request) {
        // 实际从Service创建
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponse());
    }

    /**
     * 修改评论
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @RequestBody CommentRequest request) {
        // 实际从Service更新
        return ResponseEntity.ok(new CommentResponse());
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId) {
        // 实际从Service删除
        return ResponseEntity.noContent().build();
    }
}