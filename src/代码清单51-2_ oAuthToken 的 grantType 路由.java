// 前略：package、两行 @impl 锚点注释、import 区、类注释、类声明、注入字段与构造器，见源文件
  @Override
  public ResponseEntity<TokenResponse> oAuthToken(TokenRequest body) {
    OauthClient client =
        clients
            .findByClientId(body.getClientId())
            .orElseThrow(() -> new IllegalArgumentException("INVALID_CLIENT: unknown client_id"));
    TokenRequest.GrantTypeEnum grantType = body.getGrantType();
    if (grantType == TokenRequest.GrantTypeEnum.AUTHORIZATION_CODE) {
      return exchangeAuthorizationCode(client, body);
    }
    if (grantType == TokenRequest.GrantTypeEnum.REFRESH_TOKEN) {
      return rotateRefreshToken(client, body);
    }
    throw new IllegalArgumentException("UNSUPPORTED_GRANT_TYPE: " + grantType);
  }