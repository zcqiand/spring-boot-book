@Component
public class PoolSizeCalculator {

    /**
     * 根据服务器资源计算推荐连接池大小
     * @param coreCount CPU核心数
     * @param spindleCount 有效磁盘数（SSD输入1，机械硬盘输入实际数量）
     * @param applicationType 应用类型：OLTP、BATCH、ANALYSIS
     * @return 推荐连接池大小
     */
    public int calculatePoolSize(int coreCount, int spindleCount, String applicationType) {
        // 基础公式：((核心数 * 2) + 有效磁盘数)
        int basePoolSize = (coreCount * 2) + spindleCount;

        // 根据应用类型调整
        return switch (applicationType.toUpperCase()) {
            case "OLTP" -> Math.max(basePoolSize * 2, 20);
            case "BATCH" -> Math.max(basePoolSize, 5);
            case "ANALYSIS" -> Math.max(basePoolSize, 3);
            default -> basePoolSize;
        };
    }

    /**
     * 根据预估QPS和平均查询时间计算连接池大小
     * @param qps 每秒查询数
     * @param avgQueryTimeMs 平均查询时间（毫秒）
     * @param safetyFactor 安全系数（建议0.5-0.75）
     * @return 推荐连接池大小
     */
    public int calculateByQPS(int qps, int avgQueryTimeMs, double safetyFactor) {
        // 并发连接数 = (QPS * 平均查询时间) / 1000
        double concurrentConnections = (double) qps * avgQueryTimeMs / 1000;
        return (int) Math.ceil(concurrentConnections * safetyFactor);
    }

    /**
     * 验证连接池大小是否在数据库限制内
     * @param poolSize 连接池大小
     * @param dbMaxConnections 数据库最大连接数
     * @return 是否有效
     */
    public boolean validatePoolSize(int poolSize, int dbMaxConnections) {
        int recommendedMax = (int) (dbMaxConnections * 0.8);
        if (poolSize > recommendedMax) {
            System.err.printf("警告：连接池大小%d超过数据库推荐上限%d（数据库最大连接数的80%%）%n",
                poolSize, recommendedMax);
            return false;
        }
        return true;
    }
}