// 前略：package、两行 @impl 锚点注释、import 区、类注释、类声明、注入字段与构造器，见源文件
  /** refresh_token grant：rotate（I28 未知 / 已撤销 refreshToken → 400，此前恒 200）。 */
  private ResponseEntity<TokenResponse> rotateRefreshToken(OauthClient client, TokenRequest body) {
    if (body.getRefreshToken() == null || body.getRefreshToken().isEmpty()) {
      throw new IllegalArgumentException(
          "INVALID_REQUEST: refreshToken required for grantType=refresh_token");
    }
    OauthRefreshToken rt =
        refreshTokens
            .findByRefreshToken(body.getRefreshToken())
            .orElseThrow(
                () -> new IllegalArgumentException("INVALID_GRANT: refreshToken 不存在或已被使用"));
    if (Boolean.TRUE.equals(rt.getRevoked())) {
      throw new IllegalArgumentException("INVALID_GRANT: revoked refresh_token");
    }
    // rotate：旧 rt 标 revoked
    rt.setRevoked(true);
    refreshTokens.save(rt);
    // 新 refresh 行的 client_id 以旧 rt 行绑定的值为准（登录时已过 FK 校验），不信任请求体。
    String clientId = rt.getClientId() != null ? rt.getClientId() : client.getClientId();
    String scope =
        scopeOrNull(
            accessTokens
                .findById(rt.getAccessTokenId())
                .map(OauthAccessToken::getScope)
                .orElse(null));
    String newRefresh =
        tokenIssuer.persistTokenPair(rt.getUserId(), rt.getTenantId(), clientId, scope);
    return ResponseEntity.ok(
        tokenResponse(
            jwt.issueAccessToken(rt.getUserId(), rt.getTenantId()),
            newRefresh,
            scope,
            rt.getUserId(),
            clientId,
            rt.getTenantId()));
  }