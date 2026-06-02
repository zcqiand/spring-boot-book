@Service
public class HashCacheService {
    private final StringRedisTemplate stringRedisTemplate;

    public HashCacheService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void putHash(String key, String hashKey, String value) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Map<String, String> getAllHash(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    public Long incrementHash(String key, String hashKey, long delta) {
        return stringRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }
}