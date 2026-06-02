/*
                    统一用户模型
                    ==========

    ┌──────────────────┐       ┌──────────────────┐
    │  PlatformUser   │       │   TenantUser    │
    │  (平台用户)      │       │  (租户用户)      │
    ├──────────────────┤       ├──────────────────┤
    │ id               │←──────│ platformUserId  │
    │ globalUserId    │       │ tenantId        │
    │ username        │       │ username        │
    │ email           │       │ displayName     │
    │ passwordHash    │       │ defaultRole     │
    │ status          │       │ status          │
    └──────────────────┘       └──────────────────┘
           │                          │
           │                          │
           ▼                          ▼
    ┌──────────────────┐       ┌──────────────────┐
    │ UserRole (租户)  │       │ UserPermission  │
    ├──────────────────┤       ├──────────────────┤
    │ tenantId        │       │ tenantUserId    │
    │ roleId          │       │ permissionId    │
    │ grantedBy       │       │ resourceId      │
    └──────────────────┘       └──────────────────┘
*/