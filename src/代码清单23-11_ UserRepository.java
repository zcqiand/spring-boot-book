@Repository
public class UserRepository {
    private final Map<Long, User> database = new ConcurrentHashMap<>();

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(database.values());
    }

    public User save(User user) {
        database.put(user.getId(), user);
        return user;
    }
}