@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 第一步：调用默认的OAuth2UserService获取OAuth2User
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        // 第二步：提取用户信息
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oauth2User.getName();
        String email = extractEmail(oauth2User, provider);
        String nickname = extractNickname(oauth2User, provider);

        // 第三步：查询或创建本地用户
        SocialUser user = findOrCreateUser(provider, providerId, email, nickname);

        // 第四步：返回认证对象
        return new SocialUserDetails(user, oauth2User.getAttributes());
    }

    private String extractEmail(OAuth2User oauth2User, String provider) {
        if ("google".equals(provider)) {
            return oauth2User.getAttribute("email");
        } else if ("github".equals(provider)) {
            return oauth2User.getAttribute("email");
        }
        return null;
    }

    private String extractNickname(OAuth2User oauth2User, String provider) {
        if ("google".equals(provider)) {
            return oauth2User.getAttribute("name");
        } else if ("github".equals(provider)) {
            return oauth2User.getAttribute("login");
        }
        return oauth2User.getName();
    }

    private SocialUser findOrCreateUser(String provider, String providerId,
                                        String email, String nickname) {
        // 先尝试根据社交平台ID查找
        return userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    // 检查邮箱是否已被本地用户使用
                    if (email != null) {
                        Optional<SocialUser> existingByEmail = userRepository.findByEmail(email);
                        if (existingByEmail.isPresent()) {
                            SocialUser existing = existingByEmail.get();
                            existing.setProvider(provider);
                            existing.setProviderId(providerId);
                            return userRepository.save(existing);
                        }
                    }
                    // 创建新用户
                    SocialUser newUser = new SocialUser();
                    newUser.setProvider(provider);
                    newUser.setProviderId(providerId);
                    newUser.setEmail(email);
                    newUser.setNickname(nickname != null ? nickname : email.split("@")[0]);
                    newUser.setRole("USER");
                    newUser.setCreatedAt(java.time.LocalDateTime.now());
                    newUser.setLastLoginAt(java.time.LocalDateTime.now());
                    return userRepository.save(newUser);
                });
    }
}