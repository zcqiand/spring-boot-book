public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Optional<User> findByUsernameOptional(String username);

    List<User> findByEmailContains(String keyword);

    List<User> findByUsernameStartingWith(String prefix);

    List<User> findByUsernameEndingWith(String suffix);

    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<User> findByActive(Boolean active);

    List<User> findByActiveTrueAndUsernameStartingWith(Boolean active, String prefix);

    List<User> findByEmailContainsOrEmailContains(String a, String b);
}