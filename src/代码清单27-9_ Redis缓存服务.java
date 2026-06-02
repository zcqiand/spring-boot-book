package com.example.dal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String USER_CACHE_PREFIX = "user:";
    private static final String ORDER_CACHE_PREFIX = "order:";
    private static final long DEFAULT_TTL = 30; // minutes

    public void cacheUser(Long userId, Object user) {
        String key = USER_CACHE_PREFIX + userId;
        redisTemplate.opsForValue().set(key, user, Duration.ofMinutes(DEFAULT_TTL));
    }

    public Object getCachedUser(Long userId) {
        String key = USER_CACHE_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    public void evictUser(Long userId) {
        String key = USER_CACHE_PREFIX + userId;
        redisTemplate.delete(key);
    }

    public void cacheOrder(String orderNo, Object order) {
        String key = ORDER_CACHE_PREFIX + orderNo;
        redisTemplate.opsForValue().set(key, order, Duration.ofMinutes(DEFAULT_TTL));
    }

    public Object getCachedOrder(String orderNo) {
        String key = ORDER_CACHE_PREFIX + orderNo;
        return redisTemplate.opsForValue().get(key);
    }

    public void evictOrder(String orderNo) {
        String key = ORDER_CACHE_PREFIX + orderNo;
        redisTemplate.delete(key);
    }

    public Boolean tryLock(String lockKey, long expireTime) {
        return redisTemplate.opsForValue()
            .setIfAbsent(lockKey, "1", Duration.ofSeconds(expireTime));
    }

    public void releaseLock(String lockKey) {
        redisTemplate.delete(lockKey);
    }

    public void incrementCounter(String counterKey) {
        redisTemplate.opsForValue().increment(counterKey);
    }

    public Long getCounter(String counterKey) {
        Object value = redisTemplate.opsForValue().get(counterKey);
        return value != null ? ((Number) value).longValue() : 0L;
    }
}