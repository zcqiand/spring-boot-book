package com.xrtech.jpa.controller;

import com.xrtech.jpa.entity.User;
import com.xrtech.jpa.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器（REST API）
 *
 * @RestController = @Controller + @ResponseBody
 * 所有方法返回值直接作为HTTP响应体（JSON格式）
 */
@RestController
@RequestMapping("/api/users")  // 路由前缀
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 创建用户
     * POST /api/users
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = userService.saveUser(user);
        return ResponseEntity.ok(saved);
    }

    /**
     * 根据ID查询用户
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 查询所有用户
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    /**
     * 分页查询
     * GET /api/users/page?page=0&size=10&sort=createdAt,desc
     */
    @GetMapping("/page")
    public ResponseEntity<Page<User>> getUsersByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        // 解析排序参数（格式：字段名,asc|desc）
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        org.springframework.data.domain.Sort.Direction direction =
                sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                        ? org.springframework.data.domain.Sort.Direction.ASC
                        : org.springframework.data.domain.Sort.Direction.DESC;

        Page<User> userPage = userService.findUsersByPageSorted(page, size, sortField, direction);
        return ResponseEntity.ok(userPage);
    }

    /**
     * 根据用户名查询
     * GET /api/users/search?username=xxx
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchByUsername(@RequestParam String username) {
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(List.of(user)))
                .orElse(ResponseEntity.ok(List.of()));
    }

    /**
     * 更新用户
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.findUserById(id)
                .map(existing -> {
                    existing.setUsername(user.getUsername());
                    existing.setEmail(user.getEmail());
                    User updated = userService.saveUser(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 删除用户
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        if (userService.existsUser(id)) {
            userService.deleteUser(id);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        }
        response.put("error", "用户不存在");
        return ResponseEntity.notFound().build();
    }
}