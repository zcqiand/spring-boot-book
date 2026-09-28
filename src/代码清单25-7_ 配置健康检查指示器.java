@Component
public class HikariCPHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public HikariCPHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        if (!(dataSource instanceof HikariDataSource hikariDataSource)) {
            return Health.unknown().build();
        }

        try {
            HikariPoolMXBean pool = hikariDataSource.getHikariPoolMXBean();
            int active = pool.getActiveConnections();
            int total = pool.getTotalConnections();
            int max = hikariDataSource.getMaximumPoolSize();

            double usageRate = (double) active / max;

            if (usageRate > 0.8) {
                return Health.down()
                    .withDetail("reason", "Connection pool usage rate > 80%")
                    .withDetail("active", active)
                    .withDetail("max", max)
                    .build();
            }

            return Health.up()
                .withDetail("active", active)
                .withDetail("idle", pool.getIdleConnections())
                .withDetail("total", total)
                .withDetail("max", max)
                .build();

        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}