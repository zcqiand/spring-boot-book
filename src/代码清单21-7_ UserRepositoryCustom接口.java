public interface UserRepositoryCustom {
    List<User> findInactiveUsers(int days, LocalDateTime since);
    Object[] getUserStatistics();
}