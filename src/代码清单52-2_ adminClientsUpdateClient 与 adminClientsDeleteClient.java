// 前略：package、一行 @impl 锚点注释、import 区、类 Javadoc、类声明与构造器，见源文件
  @Override
  public ResponseEntity<OAuthClient> adminClientsUpdateClient(
      String clientId, UpdateOAuthClientRequest body) {
    OauthClient e =
        clients
            .findByClientId(clientId)
            .orElseThrow(() -> new NoSuchElementException("client " + clientId));
    if (body.getClientName() != null) e.setClientName(body.getClientName());
    if (body.getRedirectUris() != null) e.setRedirectUris(body.getRedirectUris());
    if (body.getScopes() != null) e.setScopes(body.getScopes());
    return ResponseEntity.ok(toDto(clients.save(e)));
  }

  @Override
  public ResponseEntity<Void> adminClientsDeleteClient(String clientId) {
    OauthClient e =
        clients
            .findByClientId(clientId)
            .orElseThrow(() -> new NoSuchElementException("client " + clientId));
    clients.deleteById(e.getId());
    return ResponseEntity.noContent().build();
  }