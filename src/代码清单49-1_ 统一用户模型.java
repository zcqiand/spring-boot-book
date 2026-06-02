package com.lab.iam.entity;

@Entity
@Table(name = "platform_user")
@Data
public class PlatformUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String globalUserId; // 全局唯一标识（UUID）

    @Column(unique = true, nullable = false)
    private String username; // 平台用户名

    @Column(nullable = false)
    private String passwordHash;

    @Column(unique = true)
    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserSource source; // LOCAL, GOOGLE, OKTA, AZURE_AD

    private String externalIdPId; // 外部IDP标识

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
}