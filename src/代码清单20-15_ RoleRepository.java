@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);

    @Query("SELECT DISTINCT r FROM Role r LEFT JOIN FETCH r.users")
    List<Role> findAllWithUsers();

    List<Role> findByRoleNameIn(Set<String> roleNames);

    boolean existsByRoleName(String roleName);
}