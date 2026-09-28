  /** 签 access token (允许覆盖 ttl, /token endpoint 用 3600s 默认值)。 */
  public String issueAccessToken(UUID userId, UUID tenantId, long ttlSecondsOverride) {
    try {
      Instant now = Instant.now();
      JWTClaimsSet claims =
          new JWTClaimsSet.Builder()
              .issuer(issuer)
              .audience(audience)
              .subject(userId.toString())
              .claim("tenant_id", tenantId.toString())
              .jwtID(UUID.randomUUID().toString())
              .issueTime(Date.from(now))
              .notBeforeTime(Date.from(now))
              .expirationTime(Date.from(now.plusSeconds(ttlSecondsOverride)))
              .build();
      SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
      jwt.sign(signer);
      return jwt.serialize();
    } catch (JOSEException e) {
      throw new IllegalStateException("Failed to sign access token", e);
    }
  }

// 后略：测试 helper 与 refresh token 生成方法，见源文件