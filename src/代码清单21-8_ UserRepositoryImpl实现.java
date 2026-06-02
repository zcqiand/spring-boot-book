@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findInactiveUsers(int days, LocalDateTime since) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        String jpql = """
            SELECT u FROM User u
            WHERE u.createdAt < :since
              AND (u.lastLoginAt IS NULL OR u.lastLoginAt < :threshold)
            ORDER BY u.lastLoginAt ASC NULLS FIRST
            """;
        return em.createQuery(jpql, User.class)
            .setParameter("threshold", threshold)
            .setParameter("since", since)
            .getResultList();
    }

    @Override
    public Object[] getUserStatistics() {
        String sql = """
            SELECT COUNT(*),
                   SUM(CASE WHEN active = 1 THEN 1 ELSE 0 END),
                   SUM(CASE WHEN active = 0 THEN 1 ELSE 0 END)
            FROM t_user
            """;
        return (Object[]) em.createNativeQuery(sql).getSingleResult();
    }
}