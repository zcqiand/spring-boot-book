@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DataPermissionAspect {

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private PermissionFilterBuilder filterBuilder;

    @Around("@annotation(dataPermission)")
    public Object aroundDataAccess(ProceedingJoinPoint joinPoint,
                                   DataPermission dataPermission) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取当前用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("未认证");
        }

        Long userId = extractUserId(auth);
        String tenantId = tenantContext.getCurrentTenant();

        // 验证租户访问权限
        validateTenantAccess(tenantId, userId);

        // 构建权限过滤条件
        Criteria filter = filterBuilder.buildFilter(dataPermission.resourceType(), userId);

        // 将过滤条件注入方法参数
        Object[] args = injectFilter(joinPoint.getArgs(), filter, signature.getParameterNames());

        return joinPoint.proceed(args);
    }

    private Object[] injectFilter(Object[] args, Criteria filter,
                                   String[] paramNames) {
        Object[] newArgs = args.clone();

        for (int i = 0; i < paramNames.length; i++) {
            if ("filter".equals(paramNames[i]) || "criteria".equals(paramNames[i])) {
                newArgs[i] = filter;
            }
        }

        return newArgs;
    }
}