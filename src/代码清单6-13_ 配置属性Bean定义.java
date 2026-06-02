package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 数据库配置属性类
 * 使用@ConfigurationProperties将前缀为"app.database"的配置绑定到此Bean
 */
@Component
@ConfigurationProperties(prefix = "app.database")
public class DatabaseProperties {

    // 对应配置项：app.database.pool.minimum-size
    private int minimumSize = 5;

    // 对应配置项：app.database.pool.maximum-size
    private int maximumSize = 20;

    // 对应配置项：app.database.pool.connection-timeout
    private long connectionTimeout = 30000;

    // 对应配置项：app.database.pool.idle-timeout
    private long idleTimeout = 600000;

    // 对应配置项：app.database.pool.max-lifetime
    private long maxLifetime = 1800000;

    // 省略getter和setter
    public int getMinimumSize() { return minimumSize; }
    public void setMinimumSize(int minimumSize) { this.minimumSize = minimumSize; }

    public int getMaximumSize() { return maximumSize; }
    public void setMaximumSize(int maximumSize) { this.maximumSize = maximumSize; }

    public long getConnectionTimeout() { return connectionTimeout; }
    public void setConnectionTimeout(long connectionTimeout) { this.connectionTimeout = connectionTimeout; }

    public long getIdleTimeout() { return idleTimeout; }
    public void setIdleTimeout(long idleTimeout) { this.idleTimeout = idleTimeout; }

    public long getMaxLifetime() { return maxLifetime; }
    public void setMaxLifetime(long maxLifetime) { this.maxLifetime = maxLifetime; }
}