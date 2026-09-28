  @Override
  public ResponseEntity<TenantMemberView> tenantMembersInviteTenantUser(
      String tenantId, TenantMembersInviteTenantUserRequest body) {
    tenantGuard.verifyPathTenant(tenantId);
    // I42 方案 C：邀请建真 sys_user（status=2 invited）+ member（status=1 active）。
    // email 缺失 fail-fast 400（ADR-0019：禁止兜底字面量）。
    // ADR-0032：invitations 保持嵌套 TenantMemberView 不动。
    String email = body == null || body.getEmail() == null ? "" : body.getEmail().trim();
    if (email.isEmpty()) {
      throw new IllegalArgumentException("email is required");
    }
    OffsetDateTime now = OffsetDateTime.now();
    SysUser u = new SysUser();
    // 禁止手动 setId（@GeneratedValue UUID）—— merge 会当 detached 走乐观锁
    // （ObjectOptimisticLockingFailureException，memory: springboot-write-path-double-bug）。
    u.setUsername(email); // 家族约定：invitation 的 username = email（memberName 同源）
    u.setPassword(""); // notNull 列；受邀用户尚无凭据
    u.setEmail(email);
    u.setStatus(MemberStatusMapper.DB_INVITED);
    u.setFailedAttempts(0);
    u.setCreatedAt(now);
    u.setUpdatedAt(now);
    u = users.save(u);
    TenantMember e = new TenantMember();
    e.setTenantId(UUID.fromString(tenantId));
    e.setUserId(u.getId());
    e.setMemberName(email); // 家族约定：invitation 响应 memberName = username（= email）
    e.setIsOwner(false);
    e.setStatus(
        MemberStatusMapper.DB_ACTIVE); // member 立即 active（I42 oracle：user=invited, member=active）
    e.setCreatedAt(now);
    e.setUpdatedAt(now);
    return ResponseEntity.ok(toNestedView(members.save(e), u));
  }