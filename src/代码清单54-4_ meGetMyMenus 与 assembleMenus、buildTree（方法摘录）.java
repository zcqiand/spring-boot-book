  /**
   * M04.F04.I08 — 当前用户有效菜单装配。
   *
   * <p>userId 提取路径（M04.F04 §2.3 + ADR-0020 路线 A）：
   *
   * <ul>
   *   <li>生产：JwtAuthenticationToken.getToken().getSubject() === userId（UUID string）
   *   <li>测试：@WithMockUser 时 principal 是 User，getName() 返回 username（仍可作为 lookup key）
   *   <li>无认证：返回 Map.of()
   * </ul>
   */
  @Override
  public ResponseEntity<Map<String, List<EffectiveMenuNode>>> meGetMyMenus(String clientId) {
    UUID userId = currentUserId();
    if (userId == null) {
      return ResponseEntity.ok(Map.of());
    }
    return ResponseEntity.ok(assembleMenus(userId));
  }

  // 中略：currentUserId 方法，见源文件

  /**
   * M04.F04.I08 — 内部用 me/menus 装配（未来接 JWT 后直接走这个）。 当前未挂 controller（skeleton 用 meGetMyMenus 返回
   * Map.of()）。
   */
  public Map<String, List<EffectiveMenuNode>> assembleMenus(UUID userId) {
    // 1. tenant_member
    List<TenantMember> myMemberships = members.findByUserId(userId);
    if (myMemberships.isEmpty()) {
      return Map.of();
    }

    // 2. member → roles
    List<UUID> memberIds = myMemberships.stream().map(TenantMember::getId).toList();
    List<TenantMemberRole> bindings = memberRoles.findByMemberIds(memberIds);
    if (bindings.isEmpty()) {
      return Map.of();
    }
    List<UUID> roleIds = bindings.stream().map(TenantMemberRole::getRoleId).toList();

    // 3. role → menus
    List<SysRoleMenu> grants = roleMenus.findByRoleIds(roleIds);
    if (grants.isEmpty()) {
      return Map.of();
    }
    List<UUID> menuIds = grants.stream().map(SysRoleMenu::getMenuId).toList();

    // 4. menus → flat list
    List<SysMenu> flat = menus.findAllById(menuIds);

    // 5. group by clientId, build tree per client
    Map<String, List<SysMenu>> byClient =
        flat.stream().collect(Collectors.groupingBy(SysMenu::getClientId));

    Map<String, List<EffectiveMenuNode>> result = new HashMap<>();
    for (var entry : byClient.entrySet()) {
      result.put(entry.getKey(), buildTree(entry.getValue()));
    }
    return result;
  }

  private List<EffectiveMenuNode> buildTree(List<SysMenu> flat) {
    Map<UUID, EffectiveMenuNode> byId = new HashMap<>();
    List<EffectiveMenuNode> roots = new ArrayList<>();

    // 1. flat → DTO（根节点 parentId 零值 sentinel → null，对齐 msw/nextjs/aspnetcore 实测：
    // EffectiveMenuNode.parentId 序列化为 null 而非 00000000-... 零值 UUID）
    for (SysMenu m : flat) {
      EffectiveMenuNode n = new EffectiveMenuNode();
      n.setId(m.getId());
      n.setClientId(m.getClientId());
      n.setParentId(isZeroUuid(m.getParentId()) ? null : m.getParentId());
      n.setTitle(m.getTitle());
      n.setType(TypeMapper.fromShort(m.getType()));
      n.setPath(m.getPath());
      n.setComponent(m.getComponent());
      n.setPerms(m.getPerms());
      n.setIcon(m.getIcon());
      n.setSortOrder(m.getSortOrder());
      n.setChildren(new ArrayList<>());
      byId.put(m.getId(), n);
    }

    // 2. tree 装配：parentId 为 null（含已归零的根 sentinel）的为 root
    for (EffectiveMenuNode n : byId.values()) {
      if (n.getParentId() == null) {
        roots.add(n);
      } else {
        EffectiveMenuNode parent = byId.get(n.getParentId());
        if (parent != null) {
          parent.getChildren().add(n);
        } else {
          // 孤儿（parent 不在 grants 内）→ 当 root
          roots.add(n);
        }
      }
    }

    // 3. sort by sortOrder
    roots.sort(
        (a, b) ->
            Integer.compare(
                a.getSortOrder() == null ? 0 : a.getSortOrder(),
                b.getSortOrder() == null ? 0 : b.getSortOrder()));
    return roots;
  }

  // 后略：isZeroUuid 辅助方法与类收尾，见源文件