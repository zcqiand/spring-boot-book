package com.xrtech.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 图书创建请求DTO
 * 使用record实现（Java 17+）
 */
public record BookCreateRequest(
        @NotBlank(message = "书名不能为空")
        String title,

        @NotBlank(message = "作者不能为空")
        String author,

        @Pattern(regexp = "^\\d{10}$|^\\d{13}$", message = "ISBN格式不正确")
        String isbn,

        @Positive(message = "价格必须为正数")
        @DecimalMin(value = "0.01", message = "价格最小为0.01")
        BigDecimal price,

        @Size(max = 500, message = "描述不能超过500字符")
        String description
) {}

/**
 * 图书更新请求DTO（支持部分更新）
 */
public record BookUpdateRequest(
        @Size(min = 1, max = 200, message = "书名长度在1-200之间")
        String title,

        @Size(min = 1, max = 100, message = "作者名长度在1-100之间")
        String author,

        @Pattern(regexp = "^\\d{10}$|^\\d{13}$", message = "ISBN格式不正确")
        String isbn,

        @Positive(message = "价格必须为正数")
        BigDecimal price,

        @Size(max = 500, message = "描述不能超过500字符")
        String description
) {}

/**
 * 图书响应DTO
 */
public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        BigDecimal price,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}