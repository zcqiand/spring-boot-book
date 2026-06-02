package com.xrtech.chapter11.controller;

import com.xrtech.chapter11.dto.*;
import com.xrtech.chapter11.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理REST控制器
 * 完整示例：包含CRUD所有操作
 *
 * API端点：
 * GET    /api/v1/users        - 查询所有用户
 * GET    /api/v1/users/{id}   - 根据ID查询用户
 * POST   /api/v1/users       - 创建用户
 * PUT    /api/v1/users/{id}   - 完整更新用户
 * PATCH  /api/v1/users/{id}   - 部分更新用户
 * DELETE /api/v1/users/{id}   - 删除用户
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询所有用户
     * GET /api/v1/users
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * 根据ID查询用户
     * GET /api/v1/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 创建用户
     * POST /api/v1/users
     *
     * @RequestBody 将JSON请求体反序列化为UserCreateRequest对象
     * @Valid 触发JSR-303验证
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request) {
        UserResponse created = userService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/users/" + created.getId())
                .body(created);
    }

    /**
     * 完整更新用户（PUT要求提供所有字段）
     * PUT /api/v1/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse updated = userService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * 部分更新用户（PATCH只需提供要更新的字段）
     * PATCH /api/v1/users/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> patchUser(
            @PathVariable Long id,
            @RequestBody UserPatchRequest request) {
        UserResponse patched = userService.patch(id, request);
        return ResponseEntity.ok(patched);
    }

    /**
     * 删除用户
     * DELETE /api/v1/users/{id}
     * 返回204 No Content表示成功且无响应体
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}