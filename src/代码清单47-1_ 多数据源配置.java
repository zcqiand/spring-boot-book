@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource(TenantContext tenantContext) {
        // 根据租户上下文动态选择数据源
        String tenantId = tenantContext.getCurrentTenant();
        if (tenantId == null) {
            return getDefaultDataSource();
        }

        // 租户数据源缓存
        return tenantDataSourceHolder.getDataSource(tenantId);
    }
}

@Component
public class TenantDataSourceHolder {

    private final Map<String, DataSource> dataSourceCache = new ConcurrentHashMap<>();

    public DataSource getDataSource(String tenantId) {
        return dataSourceCache.computeIfAbsent(tenantId, this::createDataSource);
    }

    private DataSource createDataSource(String tenantId) {
        // 从配置或配置中心获取租户数据库连接信息
        TenantDbConfig config = tenantConfigService.getDbConfig(tenantId);

        return DataSourceBuilder.create()
            .url(config.getJdbcUrl())
            .username(config.getUsername())
            .password(config.getPassword())
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .build();
    }
}