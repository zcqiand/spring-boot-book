@Query("SELECT u FROM User u WHERE u.status = :status AND u.name LIKE %:name%")
List<User> findByStatusAndNameContaining(@Param("status") String status, @Param("name") String name);