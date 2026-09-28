  @Override
  public ResponseEntity<saas.identity.shared.dto.Tenant> adminTenantsCreateTenant(
      CreateTenantRequest body) {
    Tenant t = tenants.save(toEntity(body));
    return ResponseEntity.ok(toDto(t));
  }

  // 中略：adminTenantsGetTenant、adminTenantsUpdateTenant（见代码清单48-3）
  // 与 adminTenantsDeleteTenant，见源文件

  private Tenant toEntity(CreateTenantRequest b) {
    java.time.OffsetDateTime now = java.time.OffsetDateTime.now();
    Tenant t = new Tenant();
    t.setTenantKey(b.getTenantKey());
    t.setName(b.getName());
    t.setStatus((short) 1);
    t.setCreatedAt(now);
    t.setUpdatedAt(now);
    return t;
  }

  private saas.identity.shared.dto.Tenant toDto(Tenant e) {
    saas.identity.shared.dto.Tenant d = new saas.identity.shared.dto.Tenant();
    d.setId(e.getId());
    d.setTenantKey(e.getTenantKey());
    d.setName(e.getName());
    d.setStatus(e.getStatus() == null ? null : saas.identity.shared.dto.TenantStatus.ACTIVE);
    d.setCreatedAt(e.getCreatedAt());
    d.setUpdatedAt(e.getUpdatedAt());
    return d;
  }

  // 后略：toDtos 与类收尾，见源文件