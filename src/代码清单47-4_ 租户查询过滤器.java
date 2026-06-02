@Component
public class TenantQueryFilter {

    @Autowired
    private TenantContext tenantContext;

    @Before("execution(* com.lab..repository.*.*(..))")
    public void filterTenantQuery(JoinPoint joinPoint) {
        String tenantId = tenantContext.getCurrentTenant();
        if (tenantId == null) {
            return; // 系统级查询不受租户限制
        }

        // 动态添加租户过滤条件
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Specification) {
                ((Specification) arg)
                    .toPredicate(root, criteriaQuery, criteriaBuilder)
                    .and(criteriaBuilder.equal(root.get("tenantId"), tenantId));
            }
        }
    }
}