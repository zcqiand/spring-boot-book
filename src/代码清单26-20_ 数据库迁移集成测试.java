package com.example.migration;

import com.example.migration.service.MigrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MigrationIntegrationTest {

    @Autowired
    private MigrationService migrationService;

    @Test
    void testExecuteMigration() {
        Map<String, Object> result = migrationService.executeMigration();
        assertTrue((Boolean) result.get("success"));
    }

    @Test
    void testIdempotentMigration() {
        migrationService.executeMigration();
        Map<String, Object> result2 = migrationService.executeMigration();
        assertTrue((Boolean) result2.get("success"));
    }
}