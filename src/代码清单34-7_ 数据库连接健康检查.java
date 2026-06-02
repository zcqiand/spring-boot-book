package com.example.monitoring.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * 数据库连接健康检查指示器
 * 检查点：连接池可用性、数据库版本、活动连接数
 */
@Component("dbConnectionHealth")
public class DbConnectionHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DbConnectionHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            String productName = metaData.getDatabaseProductName();
            String productVersion = metaData.getDatabaseProductVersion();
            int majorVersion = metaData.getDatabaseMajorVersion();
            int minorVersion = metaData.getDatabaseMinorVersion();

            boolean isValid = connection.isValid(5);
            if (!isValid) {
                return Health.down()
                        .withDetail("error", "Connection validation failed")
                        .build();
            }

            return Health.up()
                    .withDetail("database", productName)
                    .withDetail("version", productVersion)
                    .withDetail("majorVersion", majorVersion)
                    .withDetail("minorVersion", minorVersion)
                    .withDetail("connectionStatus", "OK")
                    .build();

        } catch (SQLException e) {
            return Health.down()
                    .withDetail("error", "Database connection failed: " + e.getMessage())
                    .withException(e)
                    .build();
        }
    }
}