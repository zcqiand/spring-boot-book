package com.xrtech.api.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserManagementController {

    // 1. 创建资源返回201 + Location头
    @PostMapping
    public ResponseEntity<UserVO> createUser(@RequestBody CreateUserRequest request) {
        // 模拟创建逻辑
        UserVO created = new UserVO(1L, request.email(), request.name());

        // 构建新资源URI
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id())
            .toUri();

        // 注意：先body()后status()会覆盖状态码
        // 正确写法：status().body()
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .location(location)
            .body(created);
    }

    // 2. 删除成功返回204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // 模拟删除逻辑
        return ResponseEntity.noContent().build();
    }

    // 3. 查询失败返回404
    @GetMapping("/{id}")
    public ResponseEntity<UserVO> getUser(@PathVariable Long id) {
        UserVO user = findUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // 4. 验证失败返回400 + 错误详情
    @PostMapping("/register")
    public ResponseEntity<ErrorResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userExists(request.email())) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("EMAIL_EXISTS", "该邮箱已被注册"));
        }
        // 注册逻辑...
        return ResponseEntity.ok(new UserVO(1L, request.email(), request.name()));
    }

    private UserVO findUserById(Long id) {
        return null; // 简化实现
    }

    private boolean userExists(String email) {
        return false; // 简化实现
    }
}

record CreateUserRequest(String email, String name) {}
record RegisterRequest(String email, String password) {}
record UserVO(Long id, String email, String name) {}
record ErrorResponse(String code, String message) {}