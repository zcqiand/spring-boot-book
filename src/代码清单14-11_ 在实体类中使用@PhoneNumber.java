package com.xrtech.api.request;

import com.xrtech.api.validation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank(message = "手机号不能为空")
    @PhoneNumber(required = true)
    private String phoneNumber;

    // 可选手机号字段
    @PhoneNumber(required = false)
    private String backupPhone;

    // ... 其他字段
}