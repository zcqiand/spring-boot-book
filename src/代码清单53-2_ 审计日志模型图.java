/*
                    审计日志模型
                    ==========

    ┌─────────────────────────────────────────────────────────────┐
    │                      AuditLog                               │
    ├─────────────────────────────────────────────────────────────┤
    │                                                             │
    │  标识信息          操作信息          结果信息                │
    │  ─────────        ─────────         ─────────               │
    │  id                actionType       result                  │
    │  timestamp         resourceType     errorMessage            │
    │  tenantId          resourceId      durationMs              │
    │  userId                                │                    │
    │                    上下文信息        │                     │
    │                    ─────────         │                     │
    │                    requestInfo      │                     │
    │                    responseInfo     │                     │
    │                    clientIp         ▼                     │
    │                    userAgent       ┌──────────────┐     │
    │                                     │  Index索引    │     │
    │                                     ├──────────────┤     │
    │                                     │ timestamp    │     │
    │                                     │ userId       │     │
    │                                     │ tenantId     │     │
    │                                     │ actionType   │     │
    │                                     └──────────────┘     │
    └─────────────────────────────────────────────────────────────┘
*/