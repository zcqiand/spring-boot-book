// 前略：package、两行 @impl 锚点注释、import 区、类注释、类声明、注入字段与构造器，见源文件
  @Override
  public ResponseEntity<OAuthAuthorize200Response> oAuthAuthorize(AuthorizeCodeRequest body) {
    OauthClient client =
        clients
            .findByClientId(body.getClientId())
            .orElseThrow(() -> new IllegalArgumentException("unknown client_id"));
    // 认证前置（四家共同语义）：无 / 坏 Bearer → 401，先于白名单校验。
    UUID userId = currentUserIdOrNull();
    if (userId == null) {
      throw new InvalidCredentialsException("Bearer sub required for authorize");
    }
    UUID tenantId = currentTenantIdOrNull();
    if (tenantId == null) {
      throw new InvalidCredentialsException("JWT tenant_id claim required for authorize");
    }
    // 2026-09-15 四家收敛：redirect 白名单校验（此前 springboot 单侧缺失）。
    // oauth_client.redirect_uris 是 csv 文本；匹配规则与 aspnetcore OAuthController /
    // nextjs authorize route 一致——精确相等，或白名单条目是请求的前缀且边界在 '?'
    // （RFC 6749 §3.1.2，lab 前端回跳带 ?from=<业务路径>）；子路径不算匹配。
    String requestedUri = body.getRedirectUri() == null ? "" : body.getRedirectUri();
    boolean redirectAllowed =
        Arrays.stream((client.getRedirectUris() == null ? "" : client.getRedirectUris()).split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .anyMatch(
                u ->
                    requestedUri.equals(u)
                        || (requestedUri.startsWith(u) && requestedUri.charAt(u.length()) == '?'));
    if (!redirectAllowed) {
      throw new IllegalArgumentException(
          "INVALID_REDIRECT_URI: " + body.getRedirectUri() + " not in oauth_client.redirect_uris");
    }
    String code = "ac_" + UUID.randomUUID();

    OauthCode row = new OauthCode();
    row.setCode(code);
    row.setClientId(client.getClientId());
    row.setUserId(userId);
    row.setTenantId(tenantId);
    row.setRedirectUri(body.getRedirectUri());
    row.setScope(body.getScope());
    row.setExpiresAt(OffsetDateTime.now().plusMinutes(5));
    codes.save(row);

    OAuthAuthorize200Response resp = new OAuthAuthorize200Response();
    resp.setCode(code);
    resp.setState(body.getState());
    return ResponseEntity.ok(resp);
  }