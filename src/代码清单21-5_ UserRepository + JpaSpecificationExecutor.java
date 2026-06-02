public interface UserRepository
    extends JpaRepository<User, Long>,
            JpaSpecificationExecutor<User> {
}