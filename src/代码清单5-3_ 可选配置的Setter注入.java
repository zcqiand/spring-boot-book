@Service
public class CacheService {
    private CacheProvider cacheProvider;

    @Autowired(required = false) // required=false表示可选
    public void setCacheProvider(CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    // 当没有注入CacheProvider时，使用本地内存缓存
    private CacheProvider getCacheProvider() {
        return this.cacheProvider != null ? this.cacheProvider : new LocalCacheProvider();
    }
}