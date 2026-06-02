@Service
public class PoolValidationService {

    private final DataSource dataSource;
    private final PoolSizeCalculator calculator;

    public PoolValidationService(DataSource dataSource, PoolSizeCalculator calculator) {
        this.dataSource = dataSource;
        this.calculator = calculator;
    }

    /**
     * 执行连接池压力测试
     * @param threadCount 并发线程数
     * @param queryCount 每个线程执行查询数
     * @return 测试结果
     */
    public Map<String, Object> runLoadTest(int threadCount, int queryCount) {
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        ConcurrentLinkedQueue<Long> responseTimes = new ConcurrentLinkedQueue<>();

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < queryCount; j++) {
                        long queryStart = System.currentTimeMillis();
                        try (Connection conn = dataSource.getConnection();
                             PreparedStatement stmt = conn.prepareStatement("SELECT 1");
                             ResultSet rs = stmt.executeQuery()) {
                            rs.next();
                            responseTimes.add(System.currentTimeMillis() - queryStart);
                            successCount.incrementAndGet();
                        }
                    }
                } catch (SQLException e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown();
        long duration = System.currentTimeMillis() - startTime;

        Map<String, Object> result = new HashMap<>();
        result.put("threadCount", threadCount);
        result.put("queryCount", queryCount);
        result.put("successCount", successCount.get());
        result.put("failCount", failCount.get());
        result.put("duration", duration);
        result.put("qps", (double) successCount.get() / (duration / 1000.0));
        result.put("avgResponseTime", responseTimes.stream()
            .mapToLong(Long::longValue)
            .average()
            .orElse(0));
        result.put("maxResponseTime", responseTimes.stream()
            .mapToLong(Long::longValue)
            .max()
            .orElse(0));

        return result;
    }

    /**
     * 生成连接池配置报告
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== HikariCP Configuration Report ===\n");

        if (dataSource instanceof HikariDataSource hikari) {
            report.append(String.format("Pool Name: %s%n", hikari.getPoolName()));
            report.append(String.format("Maximum Pool Size: %d%n", hikari.getMaximumPoolSize()));
            report.append(String.format("Minimum Idle: %d%n", hikari.getMinimumIdle()));
            report.append(String.format("Connection Timeout: %dms%n", hikari.getConnectionTimeout()));
            report.append(String.format("Idle Timeout: %dms%n", hikari.getIdleTimeout()));
            report.append(String.format("Max Lifetime: %dms%n", hikari.getMaxLifetime()));
        }

        return report.toString();
    }
}