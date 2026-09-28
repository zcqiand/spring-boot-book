package com.xrtech.api.controller;

import com.xrtech.api.request.UserRequest;
import com.xrtech.api.request.UserRequest.CreateGroup;
import com.xrtech.api.request.UserRequest.UpdateGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    /**
     * 创建用户 - 触发 CreateGroup 全部验证
     */
    @PostMapping
    public String create(@RequestBody @Validated(CreateGroup.class) UserRequest request) {
        return "用户创建成功: " + request.getUsername();
    }

    /**
     * 更新用户 - 触发 UpdateGroup 验证（部分字段可选）
     */
    @PutMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @RequestBody @Validated(UpdateGroup.class) UserRequest request) {
        return "用户更新成功: " + id;
    }

    /**
     * 查询用户 - 无验证组，仅做数据查询
     */
    @GetMapping("/{id}")
    public UserRequest get(@PathVariable Long id) {
        UserRequest user = new UserRequest();
        user.setUsername("existing_user");
        user.setEmail("user@example.com");
        user.setAge(25);
        return user;
    }
}