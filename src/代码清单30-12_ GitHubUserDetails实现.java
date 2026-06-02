public class GitHubUserDetails implements OAuth2User, UserDetails {
    private final AppUser user;
    private final Map<String, Object> attributes;
    private final Collection<GrantedAuthority> authorities;

    public GitHubUserDetails(AppUser user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
        this.authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );
    }

    @Override
    public Map<String, Object> getAttributes() { return attributes; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override public String getPassword() { return null; }

    @Override
    public String getUsername() {
        return user.getEmail() != null ? user.getEmail() : user.getNickname();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public AppUser getUser() { return user; }
}