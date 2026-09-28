/**
 * M00.F04 角色菜单授权（I02-I04）。
 *
 * 中略：2026-09-10 I20 方案 C 的聚合返回段，见源文件
 *
 * <p>2026-09-12 I20/I38 四方并发超时+死锁修复（40P01 deadlock + StaleObjectState + 11.6s 单发）： 旧实现「派生
 * deleteByRoleId（先 SELECT 加载 34 行再逐行 DELETE）+ 逐行 save + 回读」对远端 PG （~195ms RTT）= 60+ 往返 11.6s；并发 4
 * PUT 同一 role 时「全量删+全量插+touch sys_role」互相持 junction 行锁再等对方 sys_role 行锁 → 40P01 死锁 / 3×500。对齐家族
 * oracle（nextjs route.ts notInArray 差量 delete + onConflictDoNothing insert）：
 *
 * <ol>
 *   <li>先 {@code SELECT ... FOR UPDATE} 锁 sys_role 行 —— 同 role 的并发写在此串行化， 消灭「junction 行锁 → sys_role
 *       行锁」反向等待环；
 *   <li>bulk JPQL 差量 {@code DELETE ... AND menu_id NOT IN (...)}（单语句，不动未变化的行）；
 *   <li>JdbcTemplate batch 批量 {@code INSERT ... ON CONFLICT DO NOTHING}（1 往返，幂等）；
 *   <li>响应从请求集合直接构造，不再回读。
 * </ol>
 *
 * <p>整条 PUT 现在固定 4 次往返（~0.8s @195ms RTT），并发展开后最慢一个 ~3s &lt; 契约测试 5s 预算。
 */

// 中略：类声明、字段、构造器与 GET 回查方法，见源文件

  @Override
  public ResponseEntity<RoleMenuGrant> tenantRoleMenusSetSysRoleMenus(
      String tenantId, String roleId, SetSysRoleMenusRequest body, String clientId) {
    tenantGuard.verifyPathTenant(tenantId);
    Set<UUID> requested = parseMenuIds(body);
    // 1. 行锁串行化同 role 并发写（不存在 → 404，与原 findRole 语义一致）
    UUID roleUuid = UUID.fromString(roleId);
    SysRole role = em.find(SysRole.class, roleUuid, LockModeType.PESSIMISTIC_WRITE);
    if (role == null) {
      throw new NoSuchElementException("role " + roleId);
    }
    // 2. 差量 delete —— 只清不在新集合里的 grant
    if (requested.isEmpty()) {
      roleMenus.deleteAllForRole(roleUuid);
    } else {
      roleMenus.deleteByRoleIdAndMenuIdNotIn(roleUuid, requested);
    }
    // 3. 批量 insert（ON CONFLICT DO NOTHING → 幂等，重跑不撞 23505）
    if (!requested.isEmpty()) {
      List<UUID> ordered = List.copyOf(requested);
      jdbc.batchUpdate(
          "INSERT INTO sys_role_menu (role_id, menu_id) VALUES (?, ?) ON CONFLICT DO NOTHING",
          ordered,
          100,
          (ps, menuId) -> {
            ps.setObject(1, roleUuid);
            ps.setObject(2, menuId);
          });
    }
    // 4. touch — 聚合 updatedAt 来源（家族约定）；managed 实体脏检查 commit 时 flush
    role.setUpdatedAt(OffsetDateTime.now());
    return ResponseEntity.ok(toGrantFromRequested(role, requested));
  }