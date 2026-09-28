package com.xrtech.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 用户注册请求
 * 验证组：CreateGroup（创建时全量验证）、UpdateGroup（更新时部分字段可选）
 */
public class UserRequest {

    // 创建组：必须提供；更新组：忽略
    @NotBlank(message = "用户名不能为空", groups = CreateGroup.class)
    @Size(min = 3, max = 20, message = "用户名长度3-20字符", groups = CreateGroup.class)
    private String username;

    // 两组均需验证
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    // 创建组：必须提供年龄；更新组：可选
    @NotNull(message = "年龄不能为空", groups = {CreateGroup.class, UpdateGroup.class})
    @Min(value = 18, message = "年龄必须 >= 18", groups = CreateGroup.class)
    @Max(value = 100, message = "年龄必须 <= 100", groups = CreateGroup.class)
    private Integer age;

    // 创建组：必须提供密码；更新组：可选
    @NotBlank(message = "密码不能为空", groups = CreateGroup.class)
    @Size(min = 6, max = 32, message = "密码长度6-32字符", groups = CreateGroup.class)
    private String password;

    private String nickName;

    // ========== 验证组接口 ==========
    public interface CreateGroup {}
    public interface UpdateGroup {}
}