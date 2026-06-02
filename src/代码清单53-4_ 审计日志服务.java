@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    private final BlockingQueue<AuditLog> logQueue = new LinkedBlockingQueue<>(10000);

    @PostConstruct
    public void init() {
        // 启动异步写入线程
        Executors.newSingleThreadExecutor().submit(this::asyncWriteLoop);
    }

    /**
     * 异步记录日志
     */
    public void recordAsync(AuditLog auditLog) {
        if (!logQueue.offer(auditLog)) {
            log.warn("审计日志队列已满，日志丢失: {}", auditLog.getActionType());
        }
    }

    private void asyncWriteLoop() {
        List<AuditLog> batch = new ArrayList<>();

        while (true) {
            try {
                // 批量获取日志
                AuditLog log = logQueue.take();
                batch.add(log);

                // 等待凑够批量大小或超时
                while (batch.size() < 100) {
                    AuditLog peek = logQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (peek != null) {
                        batch.add(peek);
                    }
                }

                // 批量写入
                saveBatch(batch);
                batch.clear();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("审计日志写入失败", e);
            }
        }
    }

    @Transactional
    public void saveBatch(List<AuditLog> logs) {
        auditLogRepository.saveAll(logs);
        log.debug("审计日志批量写入: count={}", logs.size());
    }
}