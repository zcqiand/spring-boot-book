package com.lab.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sys_permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "perm_code", nullable = false, unique = true, length = 100)
    private String permCode;

    @Column(name = "perm_name", nullable = false, length = 100)
    private String permName;

    @Column(name = "resource_type", nullable = false, length = 20)
    private String resourceType;  // menu, button, api, data

    @Column(name = "resource_path", length = 255)
    private String resourcePath;

    @Column(length = 20)
    private String action;  // read, write, delete, execute

    @Column(name = "data_scope", length = 20)
    private String dataScope;  // all, dept, lab, own

    @Column(length = 255)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * 判断是否为功能权限（操作权限）
     */
    public boolean isFunctionPermission() {
        return "api".equals(resourceType) || "button".equals(resourceType);
    }

    /**
     * 判断是否为数据权限
     */
    public boolean isDataPermission() {
        return dataScope != null && !dataScope.isEmpty();
    }
}