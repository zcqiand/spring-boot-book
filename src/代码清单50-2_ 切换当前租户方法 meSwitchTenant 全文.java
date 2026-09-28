// 前略：package、import 区与类声明，见源文件

  /**
   * M01.F03 切换当前租户 —— 签真 HS256 token（对齐 aspnetcore MeController.Switch）。
   *
   * <p>链路：tenant 存在性（不存在 404）→ tenant_member 该 user 有 active 行（无 → 404）→
   * issueAccessToken(sub=user_id, tenant_id) 填 SwitchTenantResponse。refreshToken 用 {@link
   * saas.identity.platform.security.JwtIssuer#generateRefreshToken}（对齐 aspnetcore： switch 不持久化
   * refresh 行，rotate 语义归 /auth/refresh）。
   */
  @Override
  public ResponseEntity<SwitchTenantResponse> meSwitchTenant(String tenantId, String clientId) {
    UUID userId = currentUserId();
    if (userId == null) {
      throw new saas.identity.platform.security.InvalidCredentialsException(
          "Bearer sub required for tenant switch");
    }
    UUID tenantUuid = UUID.fromString(tenantId);
    tenants
        .findById(tenantUuid)
        .orElseThrow(() -> new NoSuchElementException("tenant " + tenantId));
    // S5 修复（2026-09-12 四方一致）：switch 门槛从「仅 active(1)」放宽为「非 disabled(0)」
    // ——对齐 msw oracle（status !== "removed"）与 aspnetcore（Status != 0）多数派口径；
    // invited/suspended 成员可切换，被移除（0）不可。
    boolean activeMember =
        members.findByUserId(userId).stream()
            .anyMatch(
                m ->
                    tenantUuid.equals(m.getTenantId())
                        && m.getStatus() != null
                        && m.getStatus() != 0);
    if (!activeMember) {
      throw new NoSuchElementException(
          "user " + userId + " is not an active member of tenant " + tenantId);
    }
    SwitchTenantResponse r = new SwitchTenantResponse();
    r.setAccessToken(jwt.issueAccessToken(userId, tenantUuid));
    r.setRefreshToken(saas.identity.platform.security.JwtIssuer.generateRefreshToken(userId));
    r.setExpiresAt(java.time.OffsetDateTime.now().plusSeconds(jwt.getTtlSeconds()));
    r.setTenantId(tenantUuid);
    // ADR-0032：SwitchTenantResponse 删 clientId 键（契约收敛，切租户后前端经 /me 拿上下文）。
    return ResponseEntity.ok(r);
  }