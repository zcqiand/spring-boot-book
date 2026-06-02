@Service
public class GitHubOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();

    public GitHubOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = defaultService.loadUser(userRequest);

        String provider = "github";
        String providerId = oauth2User.getName();
        String login = oauth2User.getAttribute("login");
        String email = oauth2User.getAttribute("email");
        String avatarUrl = oauth2User.getAttribute("avatar_url");

        // GitHub未返回email时尝试获取
        if (email == null) {
            email = fetchUserEmail(userRequest);
        }

        AppUser user = findOrCreateUser(provider, providerId, login, email, avatarUrl);
        return new GitHubUserDetails(user, oauth2User.getAttributes());
    }

    private String fetchUserEmail(OAuth2UserRequest userRequest) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET, entity, Map.class);

            if (response.getBody() != null) {
                List<Map<String, Object>> emails = (List<Map<String, Object>>) response.getBody();
                return emails.stream()
                    .filter(e -> Boolean.TRUE.equals(e.get("verified")))
                    .filter(e -> Boolean.TRUE.equals(e.get("primary")))
                    .map(e -> (String) e.get("email"))
                    .findFirst().orElse(null);
            }
        } catch (Exception e) { /* 忽略 */ }
        return null;
    }

    private AppUser findOrCreateUser(String provider, String providerId,
                                     String login, String email, String avatarUrl) {
        Optional<AppUser> existingUser = userRepository.findByProviderAndProviderId(provider, providerId);

        if (existingUser.isPresent()) {
            AppUser user = existingUser.get();
            user.setLastLoginAt(LocalDateTime.now());
            return userRepository.save(user);
        }

        if (email != null && userRepository.existsByEmail(email)) {
            Optional<AppUser> byEmail = userRepository.findByEmail(email);
            if (byEmail.isPresent()) {
                AppUser existing = byEmail.get();
                existing.setProvider(provider);
                existing.setProviderId(providerId);
                existing.setAvatarUrl(avatarUrl);
                existing.setLastLoginAt(LocalDateTime.now());
                return userRepository.save(existing);
            }
        }

        AppUser newUser = new AppUser();
        newUser.setProvider(provider);
        newUser.setProviderId(providerId);
        newUser.setEmail(email);
        newUser.setNickname(login);
        newUser.setAvatarUrl(avatarUrl);
        newUser.setLastLoginAt(LocalDateTime.now());
        return userRepository.save(newUser);
    }
}