@Service
public class ListCacheService {
    private final StringRedisTemplate stringRedisTemplate;

    public ListCacheService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Long pushToList(String key, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    public Long appendToList(String key, String value) {
        return stringRedisTemplate.opsForList().rightPush(key, value);
    }

    public List<String> rangeList(String key, long start, long end) {
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

List类型适用于需要保持顺序的场景，如最新消息队列、任务队列等。

    public String popFromList(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }
}