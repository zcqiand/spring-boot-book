@Component
public class HikariCPMonitorService {

    private final DataSource dataSource;

    public HikariCPMonitorService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, Object> getPoolStatus() {
        Map<String, Object> status = new HashMap<>();
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            HikariPool pool = hikariDataSource.getHikariPoolMXBean();

            status.put("activeConnections", pool.getActiveConnections());
            status.put("idleConnections", pool.getIdleConnections());
            status.put("totalConnections", pool.getTotalConnections());
            status.put("threadsAwaitingConnection", pool.getThreadsAwaitingConnection());
            status.put("maximumPoolSize", hikariDataSource.getMaximumPoolSize());
            status.put("minimumIdle", hikariDataSource.getMinimumIdle());
            status.put("poolName", hikariDataSource.getPoolName());
        }
        return status;
    }

    public void printPoolStatus() {
        Map<String, Object> status = getPoolStatus();
        System.out.println("=== HikariCP Connection Pool Status ===");
        status.forEach((key, value) ->
            System.out.printf("%s: %s%n", key, value));
    }
}