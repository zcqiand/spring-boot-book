// 前略：package、一行 @impl 锚点注释、import 区、类 Javadoc、类声明与构造器，见源文件
  @Override
  public ResponseEntity<OAuthClient> adminClientsCreateClient(CreateOAuthClientRequest body) {
    OauthClient e = new OauthClient();
    e.setClientId(body.getClientId());
    e.setClientSecret(body.getClientSecret());
    e.setClientName(body.getClientName());
    e.setGrantTypes(body.getGrantTypes());
    e.setRedirectUris(body.getRedirectUris());
    e.setScopes(body.getScopes());
    // NOT NULL 列必须显式赋值（Hibernate scaffold 不推 NotNull 默认值，同 23502 教训）。
    // SSOT CreateOAuthClientRequest 里 validity 是可选 int32 —— 缺省/非正落家族应用层默认
    // 3600/86400（msw oracle 与 nextjs 同口径；不是 DB 列 DEFAULT 的 7200/2592000，
    // 2026-09-12 four-way I45 分叉修复）。
    e.setAccessTokenValidity(
        body.getAccessTokenValidity() != null && body.getAccessTokenValidity() > 0
            ? body.getAccessTokenValidity()
            : 3600);
    e.setRefreshTokenValidity(
        body.getRefreshTokenValidity() != null && body.getRefreshTokenValidity() > 0
            ? body.getRefreshTokenValidity()
            : 86400);
    e.setAutoApprove(body.getAutoApprove() != null ? body.getAutoApprove() : false);
    e.setStatus((short) 1);
    e.setCreatedAt(OffsetDateTime.now());
    e.setUpdatedAt(OffsetDateTime.now());
    OauthClient saved = clients.save(e);
    return ResponseEntity.ok(toDto(saved));
  }