package com.example.demo.config;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * HikariCP连接池配置属性类
 * 配置前缀：spring.datasource.hikari
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class HikariPoolProperties {

    private int maximumPoolSize = 10;
    private int minimumIdle = 5;
    private long connectionTimeout = 30000;
    private long idleTimeout = 600000;
    private long maxLifetime = 1800000;
    private String connectionTestQuery = "SELECT 1";
    private boolean autoCommit = true;
    private String poolName = "HikariPool-demo";

    // 省略getter和setter
    public int getMaximumPoolSize() { return maximumPoolSize; }
    public void setMaximumPoolSize(int maximumPoolSize) { this.maximumPoolSize = maximumPoolSize; }

    public int getMinimumIdle() { return minimumIdle; }
    public void setMinimumIdle(int minimumIdle) { this.minimumIdle = minimumIdle; }

    public long getConnectionTimeout() { return connectionTimeout; }
    public void setConnectionTimeout(long connectionTimeout) { this.connectionTimeout = connectionTimeout; }

    public long getIdleTimeout() { return idleTimeout; }
    public void setIdleTimeout(long idleTimeout) { this.idleTimeout = idleTimeout; }

    public long getMaxLifetime() { return maxLifetime; }
    public void setMaxLifetime(long maxLifetime) { this.maxLifetime = maxLifetime; }

    public String getConnectionTestQuery() { return connectionTestQuery; }
    public void setConnectionTestQuery(String connectionTestQuery) { this.connectionTestQuery = connectionTestQuery; }

    public boolean isAutoCommit() { return autoCommit; }
    public void setAutoCommit(boolean autoCommit) { this.autoCommit = autoCommit; }

    public String getPoolName() { return poolName; }
    public void setPoolName(String poolName) { this.poolName = poolName; }

    /**
     * 将配置转换为HikariConfig
     */
    public HikariConfig toHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(this.maximumPoolSize);
        config.setMinimumIdle(this.minimumIdle);
        config.setConnectionTimeout(this.connectionTimeout);
        config.setIdleTimeout(this.idleTimeout);
        config.setMaxLifetime(this.maxLifetime);
        config.setConnectionTestQuery(this.connectionTestQuery);
        config.setAutoCommit(this.autoCommit);
        config.setPoolName(this.poolName);
        return config;
    }
}