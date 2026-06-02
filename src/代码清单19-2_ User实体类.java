package com.xrtech.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * JPA注解说明：
 * - @Entity：标记为JPA管理的实体类
 * - @Table：指定映射的数据库表名（可选，不指定时默认表名为类名）
 * - @Id：标记主键字段
 * - @GeneratedValue：主键生成策略
 * - @Column：字段映射配置（列名、长度、是否可空等）
 */
@Entity
@Table(name = "users")  // 数据库表名
@Getter                  // Lombok：自动生成getter方法
@Setter                  // Lombok：自动生成setter方法
@NoArgsConstructor       // Lombok：生成无参构造函数
@AllArgsConstructor      // Lombok：生成全参构造函数
@Builder                 // Lombok：生成Builder模式Builder()
public class User {

    /**
     * 主键
     * - strategy = GenerationType.IDENTITY：自增主键（MySQL的AUTO_INCREMENT）
     * - 适用于H2、MySQL等支持自增的数据库
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户名
     * - unique = true：该字段值不能重复
     * - nullable = false：该字段值不能为空
     * - length = 50：数据库列长度为50字符
     */
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    /**
     * 邮箱
     * - unique = true：邮箱唯一
     */
    @Column(name = "email", unique = true, length = 100)
    private String email;

    /**
     * 创建时间
     * - updatable = false：该字段创建后不可更新（即插入后不再修改）
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     * - nullable = false：默认当前时间
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 生命周期的回调方法
     * - @PrePersist：在insert操作前执行，用于初始化字段
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * 生命周期的回调方法
     * - @PreUpdate：在update操作前执行，用于更新字段
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}