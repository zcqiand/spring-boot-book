@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final GitHubOAuth2UserService githubUserService;

    public SecurityConfig(GitHubOAuth2UserService githubUserService) {
        this.githubUserService = githubUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(githubUserService))
                        .successHandler(this::handleSuccess)
                        .failureUrl("/login?error=true")
                )
                .build();
    }

    private void handleSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        GitHubUserDetails userDetails = (GitHubUserDetails) authentication.getPrincipal();
        AppUser user = userDetails.getUser();

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "GitHub登录成功");
        result.put("user", Map.of(
            "id", user.getId(),
            "nickname", user.getNickname(),
            "email", user.getEmail() != null ? user.getEmail() : "未提供",
            "provider", user.getProvider()
        ));

        new ObjectMapper().writeValue(response.getWriter(), result);
    }
}