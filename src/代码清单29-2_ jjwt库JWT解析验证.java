var parser = Jwts.parser()
    .verifyWith(secretKey)
    .build();

var claims = parser
    .parseSignedClaims(token)
    .getPayload();

String userId = claims.getSubject();
String username = claims.get("username", String.class);
List<String> roles = claims.get("roles", List.class);
Date expiration = claims.getExpiration();