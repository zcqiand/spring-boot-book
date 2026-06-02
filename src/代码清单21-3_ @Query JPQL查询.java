@Query("SELECT u FROM User u WHERE u.username LIKE %?1% ORDER BY u.createdAt DESC")
List<User> searchByUsernameLike(String keyword);

@Query("""
    SELECT u FROM User u
    WHERE u.active = :active
      AND u.createdAt BETWEEN :startTime AND :endTime
      AND (:email IS NULL OR u.email LIKE %:email%)
    ORDER BY u.createdAt DESC
    """)
List<User> complexSearch(
    @Param("active") Boolean active,
    @Param("startTime") LocalDateTime startTime,
    @Param("endTime") LocalDateTime endTime,
    @Param("email") String email);

@Query("UPDATE User u SET u.active = true WHERE u.id IN :ids")
@Modifying
int activateUsers(@Param("ids") List<Long> ids);