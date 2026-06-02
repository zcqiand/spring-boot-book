package com.lab.manager.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * 标注在方法或类上，启用数据权限过滤
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {
    /**
     * 资源类型：experiment, device, report, sample 等
     */
    String resourceType() default "";

    /**
     * 是否强制应用数据权限过滤
     * 如果为 false，则根据用户角色决定是否过滤
     */
    boolean enforced() default true;
}