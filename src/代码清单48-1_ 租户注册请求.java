package com.lab.tenant.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TenantRegisterRequest {

    @NotBlank(message = "企业名称不能为空")
    @Size(max = 200, message = "企业名称不能超过200字符")
    private String companyName;

    @NotBlank(message = "统一社会信用代码不能为空")
    @Pattern(regexp = "^[0-9A-Z]{18}$", message = "统一社会信用代码格式不正确")
    private String businessLicense;

    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    @NotBlank(message = "联系人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String contactPhone;

    @NotBlank(message = "联系人邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String contactEmail;

    @NotNull(message = "请选择套餐")
    private String planId;

    @NotNull(message = "请选择订阅周期")
    private SubscriptionCycle cycle;

    private String referralCode; // 推荐码，用于推荐返利
}