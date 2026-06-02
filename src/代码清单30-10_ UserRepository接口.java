public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByProviderAndProviderId(String provider, String providerId);
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
}