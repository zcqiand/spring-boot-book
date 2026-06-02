package com.example.migration.service;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;

@Service
public class MigrationService {

    @Autowired
    private DataSource dataSource;

    @Value("${spring.flyway.locations:classpath:db/migration}")
    private String locations;

    public Map<String, Object> executeMigration() {
        Map<String, Object> result = new HashMap<>();
        try {
            Flyway flyway = buildFlyway();
            flyway.migrate();
            result.put("success", true);
            result.put("message", "数据库迁移成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "迁移失败: " + e.getMessage());
        }
        return result;
    }

    public Map<String, Object> validateSchema() {
        Map<String, Object> result = new HashMap<>();
        try {
            Flyway flyway = buildFlyway();
            MigrationInfo current = flyway.info().current();
            result.put("success", true);
            result.put("currentVersion", current != null ? current.getVersion().toString() : "none");
        } catch (Exception e) {
            result.put("success", false);
        }
        return result;
    }

    private Flyway buildFlyway() {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .baselineOnMigrate(true)
                .validateOnMigrate(true)
                .load();
    }
}