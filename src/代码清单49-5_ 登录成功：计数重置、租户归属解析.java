    // 成功：重置失败计数
    user.setFailedAttempts(0);
    user.setLockedUntil(null);
    users.save(user);

    // 家族语义（nextjs login route 为准）：tenantId = 用户首个 active membership 的
    // tenant（status=1），且 tenant 本身 active。sys_user 无 tenantId 列（多租户走
    // tenant_member）。此前这里是 UUID.randomUUID() —— 随机值写 oauth_access_token
    // 违反 tenant_id FK（23503），login 500 级联全后端比对失活。
    List<TenantMember> activeMemberships =
        members.findByUserId(user.getId()).stream()
            .filter(m -> m.getStatus() != null && m.getStatus() == 1)
            .toList();
    UUID tenantId =
        activeMemberships.stream().map(TenantMember::getTenantId).findFirst().orElse(null);
    if (tenantId == null) {
      throw new org.springframework.security.access.AccessDeniedException(
          "user has no active tenant membership");
    }
    Tenant tenant = tenants.findById(tenantId).orElse(null);
    if (tenant == null || tenant.getStatus() == null || tenant.getStatus() != 1) {
      throw new org.springframework.security.access.AccessDeniedException(
          "tenant unavailable: " + tenantId);
    }
    String accessToken = jwt.issueAccessToken(user.getId(), tenantId);
    String refreshToken =
        tokenIssuer.persistTokenPair(user.getId(), tenantId, body.getClientId(), null);

// 中略：availableTenants 订阅过滤与 LoginResponse 响应组装，见源文件