try {
    var claims = parser
        .parseSignedClaims(token)
        .getPayload();
    // 认证成功
} catch (ExpiredJwtException e) {
    // Token已过期
} catch (JwtException e) {
    // 签名验证失败
}