@Service
@RequiredArgsConstructor
public class OidcProviderService {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private JwtDecoderFactory jwtDecoderFactory;

    /**
     * 构建OIDC认证URL
     */
    public String buildAuthorizationUrl(String tenantId, String providerId) {
        ClientRegistration registration = clientRegistrationRepository
            .findByRegistrationId(providerId);

        String redirectUri = registration.getRedirectUri();

        // 生成state防止CSRF
        String state = generateState(tenantId);

        // 构建认证请求参数
        Map<String, String> params = new HashMap<>();
        params.put("client_id", registration.getClientId());
        params.put("redirect_uri", redirectUri);
        params.put("response_type", "code");
        params.put("scope", String.join(" ", registration.getScopes()));
        params.put("state", state);

        return UriComponentsBuilder.fromUriString(registration.getProviderDetails().getAuthorizationUri())
            .queryParams(new MultiValueMap<>(params))
            .build()
            .toString();
    }

    /**
     * 处理OIDC回调
     */
    @Transactional
    public UserAuthResult handleCallback(String code, String state) {
        // 解析state获取租户ID
        String tenantId = parseState(state);

        // 获取token
        ClientRegistration registration = clientRegistrationRepository
            .findByRegistrationId("okta");

        Map<String, String> params = Map.of(
            "grant_type", "authorization_code",
            "code", code,
            "redirect_uri", registration.getRedirectUri(),
            "client_id", registration.getClientId(),
            "client_secret", registration.getClientSecret()
        );

        // 调用IDP获取token
        HttpEntity<MultiValueMap<String, String>> request =
            new HttpEntity<>(new MultiValueMap<>(params));

        ResponseEntity<Map> response = restTemplate.exchange(
            registration.getProviderDetails().getTokenUri(),
            HttpMethod.POST,
            request,
            Map.class
        );

        Map<String, Object> tokenResponse = response.getBody();
        String idToken = (String) tokenResponse.get("id_token");
        String accessToken = (String) tokenResponse.get("access_token");

        // 解析ID Token获取用户信息
        JwtDecoder decoder = jwtDecoderFactory.createDecoder(registration);
        Jwt jwt = decoder.decode(idToken);

        // 创建或更新平台用户
        PlatformUser user = processUserInfo(registration.getRegistrationId(), jwt);

        // 刷新租户用户映射
        refreshTenantUserMapping(tenantId, user);

        return new UserAuthResult(user, accessToken);
    }
}