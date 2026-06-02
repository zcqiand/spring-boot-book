package com.xrtech.chapter12.dto;

import jakarta.validation.constraints.*;

/**
 * 用户创建请求DTO - 展示所有常用校验注解
 */
public class CreateUserRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名3-20位")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "以字母开头的字母数字下划线")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 32, message = "密码6-32位")
    private String password;

    @Min(value = 18, message = "年龄至少18岁")
    @Max(value = 120, message = "年龄不能超过120")
    private int age;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotNull(message = "地址不能为空")
    private String address;

    @DecimalMin(value = "0.01", message = "余额至少0.01")
    @DecimalMax(value = "999999.99", message = "余额不能超过999999.99")
    private BigDecimal balance;
}