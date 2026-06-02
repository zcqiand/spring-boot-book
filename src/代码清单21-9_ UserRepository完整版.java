public interface UserRepository
    extends JpaRepository<User, Long>,
            JpaSpecificationExecutor<User>,
            UserRepositoryCustom {
    Optional<User> findByUsername(String username);
    List<User> findByEmailContains(String keyword);
}