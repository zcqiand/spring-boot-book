@Service
public class CacheProtectionService {
    private final StringRedisTemplate stringRedisTemplate;

    public CacheProtectionService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 缓存击穿防护：使用分布式锁
     */
    public String getWithLock(String key, String lockKey) {
        Boolean acquired = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", Duration.ofSeconds(10));

        if (Boolean.TRUE.equals(acquired)) {
            try {
                String result = queryDatabase(key);
                stringRedisTemplate.opsForValue().set(key, result, Duration.ofMinutes(30));
                return result;
            } finally {
                stringRedisTemplate.delete(lockKey);
            }
        } else {
            try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) {}
            String cached = stringRedisTemplate.opsForValue().get(key);
            if (cached != null) return cached;
            return getWithLock(key, lockKey + "_retry");
        }
    }

    /**
     * 缓存雪崩防护：随机过期时间
     */
    public void setWithRandomExpire(String key, String value, int baseExpireMinutes) {
        int randomExtra = (int) (Math.random() * 5);
        Duration expireTime = Duration.ofMinutes(baseExpireMinutes + randomExtra);
        stringRedisTemplate.opsForValue().set(key, value, expireTime);
    }

    private String queryDatabase(String key) {
        return "数据库数据：" + key;
    }
}