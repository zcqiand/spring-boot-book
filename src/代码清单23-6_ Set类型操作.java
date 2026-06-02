@Service
public class SetCacheService {
    private final StringRedisTemplate stringRedisTemplate;

    public SetCacheService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Long addToSet(String key, String... members) {
        return stringRedisTemplate.opsForSet().add(key, members);
    }

    public Set<String> getAllSet(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    public Boolean isMemberOfSet(String key, String member) {
        return stringRedisTemplate.opsForSet().isMember(key, member);
    }

    public Set<String> intersectSets(String key, String otherKey) {
        return stringRedisTemplate.opsForSet().intersect(key, otherKey);
    }
}