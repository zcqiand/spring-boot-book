  @Override
  public ResponseEntity<saas.identity.shared.dto.Tenant> adminTenantsUpdateTenant(
      String id, UpdateTenantRequest body) {
    UUID uuid = UUID.fromString(id);
    Tenant t = tenants.findById(uuid).orElseThrow(() -> new NoSuchElementException("tenant " + id));
    if (body.getName() != null) t.setName(body.getName());
    if (body.getStatus() != null)
      t.setStatus(
          body.getStatus() == saas.identity.shared.dto.TenantStatus.ACTIVE ? (short) 1 : (short) 0);
    return ResponseEntity.ok(toDto(tenants.save(t)));
  }