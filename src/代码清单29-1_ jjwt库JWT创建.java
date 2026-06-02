import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

var secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
var now = new Date();
var expiration = new Date(now.getTime() + 3600000); // 1小时后过期

String token = Jwts.builder()
    .subject("user-123")
    .issuer("xr-tech-book")
    .claim("username", "zhangsan")
    .claim("roles", List.of("ADMIN", "USER"))
    .issuedAt(now)
    .expiration(expiration)
    .signWith(secretKey)
    .compact();