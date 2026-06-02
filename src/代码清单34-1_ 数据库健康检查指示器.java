package com.example.monitoring.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;

@Component("databaseHealth")
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            // 执行简单查询验证数据库连接
            boolean valid = connection.isValid(5);
            if (valid) {
                return Health.up()
                        .withDetail("database", "MySQL")
                        .withDetail("connection", "OK")
                        .withDetail("catalog", connection.getCatalog())
                        .withDetail("schema", connection.getSchema())
                        .build();
            } else {
                return Health.down()
                        .withDetail("error", "Connection invalid")
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withException(e)
                    .build();
        }
    }
}