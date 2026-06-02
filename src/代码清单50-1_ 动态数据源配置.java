@Configuration
@RequiredArgsConstructor
public class DynamicDataSourceConfig {

    @Bean
    public DataSource dynamicDataSource(TenantContext tenantContext) {
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(new HashMap<>());
        dataSource.setDefaultTargetDataSource(createDefaultDataSource());

        // 懒加载租户数据源
        dataSource.setLazyInitial(true);
        dataSource.setTenantContext(tenantContext);

        return dataSource;
    }

    private DataSource createDefaultDataSource() {
        return DataSourceBuilder.create()
            .url("jdbc:mysql://localhost:3306/lab_platform")
            .username("root")
            .password("password")
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .build();
    }
}