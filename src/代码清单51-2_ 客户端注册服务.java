@Service
@RequiredArgsConstructor
@Slf4j
public class ClientRegistrationService {

    @Autowired
    private RegisteredClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 注册新客户端
     */
    @Transactional
    public RegisteredClient registerClient(ClientRegistrationRequest request) {
        // 验证客户端ID唯一性
        if (clientRepository.findByClientId(request.getClientId()) != null) {
            throw new ClientException("客户端ID已存在");
        }

        // 构建重定向URI列表
        Set<String> redirectUris = new HashSet<>(request.getRedirectUris());

        // 构建授权类型列表
        Set<AuthorizationGrantType> grantTypes = new HashSet<>();
        for (String grantType : request.getGrantTypes()) {
            grantTypes.add(
                AuthorizationGrantType.from(grantType)
                    .orElseThrow(() -> new ClientException("不支持的授权类型"))
            );
        }

        // 构建scope列表
        Set<Scope> scopes = new HashSet<>();
        for (String scope : request.getScopes()) {
            scopes.add(new Scope(scope));
        }

        RegisteredClient client = RegisteredClient
            .withId(UUID.randomUUID().toString())
            .clientId(request.getClientId())
            .clientSecret(passwordEncoder.encode(request.getClientSecret()))
            .clientAuthenticationMethod(
                ClientAuthenticationMethod.from(
                    request.getClientAuthenticationMethod()
                ).orElse(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            )
            .authorizationGrantTypes(types -> types.addAll(grantTypes))
            .redirectUris(uris -> uris.addAll(redirectUris))
            .scopes(s -> s.addAll(scopes))
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(
                    Duration.ofMinutes(request.getAccessTokenValidity())
                )
                .refreshTokenTimeToLive(
                    Duration.ofMinutes(request.getRefreshTokenValidity())
                )
                .build())
            .build();

        client = clientRepository.save(client);
        log.info("客户端注册成功: clientId={}", request.getClientId());

        return client;
    }

    /**
     * 验证客户端
     */
    public boolean validateClient(String clientId, String clientSecret) {
        RegisteredClient client = clientRepository.findByClientId(clientId);
        if (client == null) {
            return false;
        }

        return passwordEncoder.matches(clientSecret, client.getClientSecret());
    }
}