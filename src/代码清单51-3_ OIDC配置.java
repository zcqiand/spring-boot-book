@Configuration
@RequiredArgsConstructor
public class OidcConfiguration {

    @Autowired
    private RegisteredClientRepository clientRepository;

    /**
     * OIDC提供者配置
     */
    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder()
            .issuer("http://localhost:8080")
            .authorizationEndpoint("/oauth2/authorize")
            .tokenEndpoint("/oauth2/token")
            .jwkSetEndpoint("/oauth2/jwks")
            .userInfoEndpoint("/userinfo")
            .oidcConfigurationEndpoint("/.well-known/openid-configuration")
            .build();
    }

    /**
     * 用户信息端点实现
     */
    @Bean
    public OidcUserInfoEndpointResponse userInfoEndpoint(
            ServerHttpRequest request,
            @AuthenticationPrincipal OidcUser oidcUser) {

        Map<String, Object> claims = new HashMap<>();

        if (oidcUser != null) {
            claims.put("sub", oidcUser.getSubject());
            claims.put("name", oidcUser.getFullName());
            claims.put("email", oidcUser.getEmail());
            claims.put("locale", oidcUser.getLocale());
        }

        return new OidcUserInfoEndpointResponse(claims);
    }
}