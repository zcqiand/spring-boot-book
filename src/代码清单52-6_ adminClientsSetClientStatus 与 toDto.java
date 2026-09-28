// 前略：package、一行 @impl 锚点注释、import 区、类 Javadoc、类声明与构造器，见源文件
  @Override
  public ResponseEntity<OAuthClient> adminClientsSetClientStatus(
      String clientId, AdminClientsSetClientStatusRequest body) {
    OauthClient e =
        clients
            .findByClientId(clientId)
            .orElseThrow(() -> new NoSuchElementException("client " + clientId));
    if (body.getStatus() != null) e.setStatus(body.getStatus().shortValue());
    return ResponseEntity.ok(toDto(clients.save(e)));
  }

  private OAuthClient toDto(OauthClient e) {
    OAuthClient d = new OAuthClient();
    d.setId(e.getId());
    d.setClientId(e.getClientId());
    d.setClientName(e.getClientName());
    d.setGrantTypes(e.getGrantTypes());
    d.setRedirectUris(e.getRedirectUris());
    d.setScopes(e.getScopes());
    d.setStatus(e.getStatus() == null ? null : e.getStatus().intValue());
    d.setAutoApprove(e.getAutoApprove());
    // 2026-09-12 four-way I45 分叉修复：validity 与审计列是响应契约字段，此前漏映射
    // → 序列化 null → normalize 剔 null 后与 msw oracle（3600/86400 + 时间戳非空）分叉。
    d.setAccessTokenValidity(e.getAccessTokenValidity());
    d.setRefreshTokenValidity(e.getRefreshTokenValidity());
    d.setCreatedAt(e.getCreatedAt());
    d.setUpdatedAt(e.getUpdatedAt());
    return d;
  }