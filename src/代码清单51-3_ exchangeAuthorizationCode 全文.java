// 前略：package、两行 @impl 锚点注释、import 区、类注释、类声明、注入字段与构造器，见源文件
  /** authorization_code grant：code 一次性消费 + 过期校验（I27 重放 → 400）。 */
  private ResponseEntity<TokenResponse> exchangeAuthorizationCode(
      OauthClient client, TokenRequest body) {
    if (body.getCode() == null || body.getCode().isEmpty()) {
      throw new IllegalArgumentException(
          "INVALID_REQUEST: code required for grantType=authorization_code");
    }
    if (body.getRedirectUri() == null || body.getRedirectUri().isEmpty()) {
      throw new IllegalArgumentException(
          "INVALID_REQUEST: redirectUri required for grantType=authorization_code");
    }
    OauthCode row =
        codes
            .findByCode(body.getCode())
            .filter(c -> client.getClientId().equals(c.getClientId()))
            .orElseThrow(() -> new IllegalArgumentException("INVALID_GRANT: code 不存在或已被使用"));
    if (row.getExpiresAt() != null && row.getExpiresAt().isBefore(OffsetDateTime.now())) {
      codes.delete(row);
      throw new IllegalArgumentException("INVALID_GRANT: expired code");
    }
    // 2026-09-15 四家收敛：RFC 6749 §4.1.3——redirect_uri 必须与 authorize 时一致
    // （msw / nextjs / aspnetcore 已有此校验，springboot 此前单侧缺失）。
    if (!body.getRedirectUri().equals(row.getRedirectUri())) {
      codes.delete(row);
      throw new IllegalArgumentException("INVALID_GRANT: redirectUri mismatch");
    }
    // 一次性消费：删 code 行，防重放
    codes.delete(row);
    return ResponseEntity.ok(
        tokenResponse(
            jwt.issueAccessToken(row.getUserId(), row.getTenantId()),
            tokenIssuer.persistTokenPair(
                row.getUserId(), row.getTenantId(), client.getClientId(), row.getScope()),
            scopeOrNull(row.getScope()),
            row.getUserId(),
            client.getClientId(),
            row.getTenantId()));
  }