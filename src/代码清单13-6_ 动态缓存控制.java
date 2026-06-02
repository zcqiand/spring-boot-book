package com.xrtech.api.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;

@RestController
@RequestMapping("/api/resources")
public class CacheController {

    // 不缓存
    @GetMapping("/sensitive")
    public ResponseEntity<ResourceVO> getSensitive() {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.noCache())
            .body(new ResourceVO("sensitive data"));
    }

    // 私有缓存（浏览器缓存，CDN不缓存）
    @GetMapping("/private")
    public ResponseEntity<ResourceVO> getPrivate() {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(Duration.ofHours(1))
                .cachePrivate())
            .body(new ResourceVO("user private data"));
    }

    // ETag支持
    @GetMapping("/etag")
    public ResponseEntity<ResourceVO> getWithETag(
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        ResourceVO resource = new ResourceVO("versioned data");
        String etag = "\"v1.0.0\"";

        // 检查ETag是否匹配
        if (etag.equals(ifNoneMatch)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                .eTag(etag)
                .build();
        }

        return ResponseEntity.ok()
            .eTag(etag)
            .body(resource);
    }
}

record ResourceVO(String data) {}