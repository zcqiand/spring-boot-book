@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogAspect {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private UserContext userContext;

    @Around("@annotation(auditable)")
    public Object aroundAudit(ProceedingJoinPoint joinPoint,
                              Auditable auditable) throws Throwable {
        long start = System.currentTimeMillis();

        // 提取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String actionType = auditable.actionType();
        String resourceType = auditable.resourceType();

        // 构建请求信息
        Map<String, Object> requestInfo = buildRequestInfo(
            joinPoint, signature, auditable.includeArgs());

        String clientIp = getClientIp();
        String userAgent = getUserAgent();

        Object result = null;
        String errorMessage = null;
        String res = "SUCCESS";

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            res = "FAILURE";
            errorMessage = e.getMessage();
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - start;

            // 异步记录日志
            AuditLog log = AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .actionType(actionType)
                .resourceType(resourceType)
                .resourceId(extractResourceId(result, auditable))
                .tenantId(tenantContext.getCurrentTenant())
                .userId(userContext.getCurrentUserId())
                .username(userContext.getCurrentUsername())
                .userRole(userContext.getCurrentRole())
                .requestInfo(serializeRequest(requestInfo))
                .responseInfo(serializeResponse(result, auditable))
                .clientIp(clientIp)
                .userAgent(userAgent)
                .result(res)
                .errorMessage(errorMessage)
                .durationMs(duration)
                .build();

            auditLogService.recordAsync(log);
        }
    }

    private String extractResourceId(Object result, Auditable auditable) {
        if (result == null) {
            return null;
        }
        // 反射获取资源ID
        try {
            Method getId = result.getClass().getMethod("getId");
            Object id = getId.invoke(result);
            return id != null ? id.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}