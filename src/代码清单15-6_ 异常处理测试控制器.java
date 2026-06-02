package com.xrtech.api.controller;

import com.xrtech.api.exception.BusinessException;
import com.xrtech.api.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    /**
     * 测试业务异常
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable Long id) {
        if (id < 0) {
            throw new BusinessException<>(409, "用户ID不能为负数", HttpStatus.CONFLICT);
        }
        if (id == 0) {
            throw new ResourceNotFoundException<>("用户", id);
        }
        return ResponseEntity.ok("User-" + id);
    }
}