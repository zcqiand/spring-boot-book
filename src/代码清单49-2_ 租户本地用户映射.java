package com.lab.iam.entity;

@Entity
@Table(name = "tenant_user_mapping")
@Data
public class TenantUserMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private Long platformUserId;

    @Column(unique = true) // tenant + username 唯一
    private String username; // 租户内用户名

    private String displayName; // 显示名称

    @Enumerated(EnumType.STRING)
    private TenantRole defaultRole; // 默认角色

    private String department;

    @Enumerated(EnumType.STRING)
    private MappingStatus status; // ACTIVE, SUSPENDED, PENDING

    private LocalDateTime joinedAt;
    private LocalDateTime lastAccessedAt;
}