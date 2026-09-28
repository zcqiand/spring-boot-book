package com.xrtech.api.request;

import com.xrtech.api.validation.PasswordMatches;
import com.xrtech.api.validation.PhoneNumber;
import jakarta.validation.constraints.*;

/**
 * 用户注册请求
 * 使用 @PasswordMatches 验证密码一致性
 */
@PasswordMatches
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度3-20字符")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字、下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度6-32字符")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @PhoneNumber(required = true)
    private String phoneNumber;
}