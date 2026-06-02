@Service
@RequiredArgsConstructor
@Slf4j
public class ComplianceReportService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private ReportGenerator reportGenerator;

    /**
     * 生成合规报告
     */
    public Report generateComplianceReport(ComplianceReportRequest request) {
        // 查询审计日志
        List<AuditLog> logs = auditLogRepository.findByTenantAndTimeRange(
            request.getTenantId(),
            request.getStartTime(),
            request.getEndTime()
        );

        // 生成报告数据
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("reportPeriod",
            request.getStartTime() + " 至 " + request.getEndTime());
        reportData.put("generatedAt", LocalDateTime.now().toString());
        reportData.put("tenantId", request.getTenantId());

        // 统计概览
        reportData.put("totalOperations", logs.size());
        reportData.put("successCount",
            logs.stream().filter(l -> "SUCCESS".equals(l.getResult())).count());
        reportData.put("failureCount",
            logs.stream().filter(l -> "FAILURE".equals(l.getResult())).count());

        // 按操作类型统计
        Map<String, Long> byActionType = logs.stream()
            .collect(Collectors.groupingMapByLong(AuditLog::getActionType, Collectors.counting()));
        reportData.put("byActionType", byActionType);

        // 按资源类型统计
        Map<String, Long> byResourceType = logs.stream()
            .collect(Collectors.groupingMapByLong(AuditLog::getResourceType, Collectors.counting()));
        reportData.put("byResourceType", byResourceType);

        // 按用户统计（前10名）
        Map<Long, Long> byUser = logs.stream()
            .collect(Collectors.groupingMapByLong(AuditLog::getUserId, Collectors.counting()));
        List<Map.Entry<Long, Long>> topUsers = byUser.entrySet().stream()
            .sorted(Map.Entry.<Long, Long>comparingByValue().reverse())
            .limit(10)
            .collect(Collectors.toList());
        reportData.put("topUsers", topUsers);

        // 生成PDF或Excel
        return reportGenerator.generate(reportData, request.getFormat());

    }
}