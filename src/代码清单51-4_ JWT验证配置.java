@Configuration
@RequiredArgsConstructor
public class JwtConfiguration {

    @Autowired
    private JWKSource<SecurityContext> jwkSource;

    /**
     * 配置JWT验证器
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
            .withJwkSetUri("http://localhost:8080/oauth2/jwks")
            .jwkSource(jwkSource)
            .build();
    }

    /**
     * 验证ID Token
     */
    public boolean validateIdToken(String idToken, String expectedIssuer) {
        try {
            JwtDecoder decoder = jwtDecoder();
            Jwt jwt = decoder.decode(idToken);

            // 验证Issuer
            if (!expectedIssuer.equals(jwt.getIssuer())) {
                log.warn("ID Token Issuer不匹配: expected={}, actual={}",
                    expectedIssuer, jwt.getIssuer());
                return false;
            }

            // 验证Audience
            List<String> audience = jwt.getAudience();
            if (!audience.contains("lab-system-client")) {
                log.warn("ID Token Audience不匹配");
                return false;
            }

            // 验证过期时间
            if (jwt.getExpiresAt() != null &&
                jwt.getExpiresAt().isBefore(Instant.now())) {
                log.warn("ID Token已过期");
                return false;
            }

            return true;

        } catch (JwtException e) {
            log.warn("ID Token验证失败", e);
            return false;
        }
    }
}