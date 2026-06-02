@Service
public class CacheService {
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    private final AtomicInteger hitCount = new AtomicInteger(0);
    private final AtomicInteger missCount = new AtomicInteger(0);

    public CacheService(MeterRegistry meterRegistry) {
        Gauge.builder("exercise.cache.hit.rate", this, CacheService::calculateHitRate)
                .description("缓存命中率").register(meterRegistry);
        Gauge.builder("exercise.cache.size", cache, Map::size)
                .description("当前缓存条目数").register(meterRegistry);
    }

    public Object get(String key) {
        Object value = cache.get(key);
        if (value != null) hitCount.incrementAndGet();
        else missCount.incrementAndGet();
        return value;
    }

    public void put(String key, Object value) { cache.put(key, value); }

    public double calculateHitRate() {
        int total = hitCount.get() + missCount.get();
        return total == 0 ? 0.0 : (double) hitCount.get() / total;
    }
}