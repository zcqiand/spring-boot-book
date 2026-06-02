@Configuration
public class CustomDataSourceConfig {

    // 用户定义的DataSource永远优先于自动配置
    // 因为Spring Boot的自动配置类都使用@ConditionalOnMissingBean
    // 如果用户手动定义了这个Bean，自动配置中的DataSource就不会被注册
    // 这种方式比排除自动配置更安全，因为其他自动配置仍然生效
    // 同时也不会影响依赖这些自动配置的其他自动配置类
    // 这是一个更温和、更可控的覆盖方式
    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setMaximumPoolSize(10);
        ds.setMinimumIdle(5);
        System.out.println(">>> [用户配置] 使用自定义DataSource（HikariCP连接池）");
        return ds;
    }
}