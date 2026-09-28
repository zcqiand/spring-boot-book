  @Override
  public ResponseEntity<TenantApplication> tenantApplicationsUpdateTenantApplication(
      String tenantId, String clientId, UpdateTenantApplicationRequest body) {
    tenantGuard.verifyPathTenant(tenantId);
    UUID tenantUuid = UUID.fromString(tenantId);
    saas.identity.platform.entity.Generated.TenantApplication e =
        apps.findByTenantIdAndClientId(tenantUuid, clientId)
            .orElseThrow(
                () ->
                    new NoSuchElementException(
                        "tenant_application tenant=" + tenantId + " client=" + clientId));
    if (body.getStatus() != null) e.setStatus(body.getStatus().shortValue());
    if (body.getExpireTime() != null) e.setExpireTime(body.getExpireTime());
    return ResponseEntity.ok(toDto(apps.save(e)));
  }