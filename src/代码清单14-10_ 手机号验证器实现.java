package com.xrtech.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 手机号验证器实现
 * 规则：1开头，11位数字
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    // 中国大陆手机号正则
    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^1[3-9]\\d{9}$");

    private boolean required;

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 空值处理
        if (value == null || value.isBlank()) {
            return !required;  // required=true时不允许为空
        }

        // 格式验证
        return PHONE_PATTERN.matcher(value).matches();
    }
}