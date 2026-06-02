@Service
public class UserCacheService {
    private final UserRepository userRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private static final String USER_CACHE_PREFIX = "user:";
    private static final int USER_CACHE_EXPIRE_MINUTES = 30;

    public UserCacheService(UserRepository userRepository, StringRedisTemplate stringRedisTemplate) {
        this.userRepository = userRepository;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 带击穿防护的用户查询
     */
    public Optional<User> findByIdWithProtection(Long id) {
        String cacheKey = USER_CACHE_PREFIX + id;
        String cached = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Optional.of(deserializeUser(cached));
        }

        String lockKey = "lock:user:" + id;
        Boolean acquired = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", Duration.ofSeconds(10));

        if (Boolean.TRUE.equals(acquired)) {
            try {
                Optional<User> userOpt = userRepository.findById(id);
                userOpt.ifPresent(user -> {
                    stringRedisTemplate.opsForValue().set(cacheKey, serializeUser(user),
                        Duration.ofMinutes(USER_CACHE_EXPIRE_MINUTES + (int)(Math.random() * 5)));
                });
                return userOpt;
            } finally {
                stringRedisTemplate.delete(lockKey);
            }
        } else {
            try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) {}
            String retryCached = stringRedisTemplate.opsForValue().get(cacheKey);
            if (retryCached != null) return Optional.of(deserializeUser(retryCached));
            return findByIdWithProtection(id);
        }
    }

    @Cacheable(value = "userList", key = "'active'", unless = "#result.isEmpty()")
    public List<User> findAllActiveUsers() {
        return userRepository.findAll().stream().filter(u -> u.getStatus() == 1).toList();
    }

    @CachePut(value = "users", key = "#result.id")
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private String serializeUser(User user) {
        return user.getId() + "|" + user.getUsername() + "|" + user.getEmail();
    }

    private User deserializeUser(String data) {
        String[] parts = data.split("\\|");
        User user = new User();
        user.setId(Long.parseLong(parts[0]));
        user.setUsername(parts[1]);
        user.setEmail(parts[2]);
        return user;
    }
}