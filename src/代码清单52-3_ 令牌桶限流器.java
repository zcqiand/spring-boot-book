@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBucketRateLimiter {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 尝试获取令牌
     * @param key 限流key（如用户ID、IP）
     * @param rate 每秒补充的令牌数
     * @param capacity 桶的容量
     * @return 是否获取成功
     */
    public boolean tryAcquire(String key, int rate, int capacity) {
        String redisKey = "ratelimit:tokenbucket:" + key;

        // 获取当前令牌数
        String current = redisTemplate.opsForValue().get(redisKey);
        long tokens = current == null ? capacity : Long.parseLong(current);

        if (tokens <= 0) {
            log.debug("令牌桶已耗尽: key={}", key);
            return false;
        }

        // 尝试消费一个令牌
        Long remain = redisTemplate.opsForValue().decrement(redisKey);

        // 如果已经是负数，说明刚才的decrement把我们从1变成0或负
        // 把令牌加回去并返回失败
        if (remain != null && remain < 0) {
            redisTemplate.opsForValue().increment(redisKey);
            log.debug("令牌桶已耗尽: key={}", key);
            return false;
        }

        // 计算下次补充令牌的时间
        long now = System.currentTimeMillis();
        long nextFill = now + (1000 / rate);

        // 调度令牌补充
        scheduleTokenFill(key, rate, capacity, nextFill);

        log.debug("令牌获取成功: key={}, remain={}", key, remain);
        return true;
    }

    private void scheduleTokenFill(String key, int rate,
                                   int capacity, long nextFill) {
        String refillKey = "ratelimit:refill:" + key;

        // 如果已经调度了补充，则跳过
        Boolean exist = redisTemplate.hasKey(refillKey);
        if (Boolean.TRUE.equals(exist)) {
            return;
        }

        // 标记已调度
        redisTemplate.opsForValue().set(refillKey, "1",
            (nextFill - System.currentTimeMillis()) + 1000,
            TimeUnit.MILLISECONDS);

        // 计算需要补充的令牌数
        int refillAmount = (int) ((System.currentTimeMillis() - nextFill) / 1000 * rate);
        if (refillAmount > 0) {
            String redisKey = "ratelimit:tokenbucket:" + key;
            redisTemplate.opsForValue().increment(redisKey,
                Math.min(refillAmount, capacity));
        }
    }
}