public class DynamicDataSource extends AbstractDataSource {

    private TenantContext tenantContext;
    private Map<String, DataSource> dataSourceCache = new ConcurrentHashMap<>();

    @Override
    public Connection getConnection() throws SQLException {
        DataSource ds = resolveDataSource();
        return ds.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        DataSource ds = resolveDataSource();
        return ds.getConnection(username, password);
    }

    private DataSource resolveDataSource() {
        String tenantId = tenantContext.getCurrentTenant();
        if (tenantId == null) {
            return getDefaultTargetDataSource();
        }

        return dataSourceCache.computeIfAbsent(tenantId, this::createTenantDataSource);
    }

    private DataSource createTenantDataSource(String tenantId) {
        TenantDbConfig config = tenantConfigService.getDbConfig(tenantId);

        DataSource ds = DataSourceBuilder.create()
            .url(config.getJdbcUrl())
            .username(config.getUsername())
            .password(config.getPassword())
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .build();

        log.info("创建租户数据源: tenantId={}", tenantId);
        return ds;
    }
}