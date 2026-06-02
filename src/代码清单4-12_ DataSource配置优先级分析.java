package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // 方案一：使用自动配置的属性（通过Properties类）
    // 这种方式会使用application.yml中的spring.datasource配置
    // 由Spring Boot的DataSourceAutoConfiguration处理
    // 你不需要关心DataSource的具体实现，Spring Boot会根据依赖自动选择
    // 这是一种"委托"方式，让自动配置来处理细节
    //@Bean
    //@Primary
    //public DataSource dataSource(DataSourceProperties properties) {
    //    return properties.initializeDataSourceBuilder().build();
    //}

    // 方案二：完全自定义DataSource（会覆盖自动配置）
    // 由于@ConditionalOnMissingBean，用户定义的Bean优先
    // 这意味着application.yml中的spring.datasource配置会被忽略
    // 如果你需要完全控制DataSource的所有参数，使用这种方式
    // 注意：这个Bean会覆盖自动配置，但配置属性仍然会被读取
    @Bean
    @Primary
    public DataSource customDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:h2:mem:mydb");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setMaximumPoolSize(20);
        System.out.println(">>> [用户配置] 使用自定义DataSource，pool size=20");
        return ds;
    }
}