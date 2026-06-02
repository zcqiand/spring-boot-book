@Service
@RequiredArgsConstructor
@Slf4j
public class SlidingWindowRateLimiter {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 滑动窗口限流
     * @param key 限流key
     * @param windowSize 窗口大小（秒）
     * @param maxRequests 窗口内最大请求数
     * @return 是否允许通过
     */
    public boolean tryAcquire(String key, int windowSize, int maxRequests) {
        String redisKey = "ratelimit:sliding:" + key;
        long now = System.currentTimeMillis();
        long windowStart = now - windowSize * 1000;

        // 使用Redis有序集合实现滑动窗口
        // score: 时间戳, value: 请求ID
        String requestId = UUID.randomUUID().toString();

        // 清理窗口外的请求
        redisTemplate.opsForZSet().removeRangeByScore(
            redisKey, 0, windowStart);

        // 统计当前窗口内的请求数
        Long count = redisTemplate.opsForZSet().zCard(redisKey);

        if (count != null && count >= maxRequests) {
            log.debug("滑动窗口限流触发: key={}, count={}, max={}",
                key, count, maxRequests);
            return false;
        }

        // 添加当前请求
        redisTemplate.opsForZSet().add(redisKey, requestId, now);

        // 设置过期时间，清理僵尸数据
        redisTemplate.expire(redisKey, windowSize * 2, TimeUnit.SECONDS);

        log.debug("滑动窗口限流通过: key={}, count={}", key, count + 1);
        return true;
    }

    /**
     * 获取当前窗口内的请求数
     */
    public long getCurrentCount(String key, int windowSize) {
        String redisKey = "ratelimit:sliding:" + key;
        long now = System.currentTimeMillis();
        long windowStart = now - windowSize * 1000;

        return redisTemplate.opsForZSet()
            .count(redisKey, windowStart, now);
    }
}