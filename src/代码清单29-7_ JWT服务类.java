@Service
public class JwtService {

    @Value("${jwt.secret:xrbook-jwt-secret-key-must-be-at-least-256-bits-long-for-hs256}")
    private String secretKey;

    @Value("${jwt.access-token-expiration:900000}") // 15分钟
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:604800000}") // 7天
    private long refreshTokenExpiration;

    private final ConcurrentHashMap<String, Long> refreshTokenStore = new ConcurrentHashMap<>();

    // 生成Access Token
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("type", "access");
        extraClaims.put("roles", extractRoles(userDetails));
        return buildToken(extraClaims, userDetails.getUsername(), accessTokenExpiration);
    }

    // 生成Refresh Token
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("type", "refresh");
        String refreshToken = buildToken(extraClaims, userDetails.getUsername(), refreshTokenExpiration);
        refreshTokenStore.put(refreshToken, System.currentTimeMillis());
        return refreshToken;
    }

    private String buildToken(Map<String, Object> extraClaims, String subject, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = extractAllClaims(refreshToken);
            String type = claims.get("type", String.class);
            return "refresh".equals(type) && !isTokenExpired(refreshToken);
        } catch (Exception e) {
            return false;
        }
    }

    public String refreshAccessToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh Token无效或已过期");
        }
        String username = extractUsername(refreshToken);
        return generateAccessToken(loadUserByUsername(username));
    }

    private UserDetails loadUserByUsername(String username) {
        return userDetailsService.loadUserByUsername(username);
    }

    public void revokeRefreshToken(String refreshToken) {
        refreshTokenStore.remove(refreshToken);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private final UserDetailsService userDetailsService;
    public JwtService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}