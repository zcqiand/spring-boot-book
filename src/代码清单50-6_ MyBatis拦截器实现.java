@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantSqlInterceptor implements Interceptor {

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private PermissionFilterBuilder filterBuilder;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = handler.getBoundSql();
        String sql = boundSql.getSql();

        // 解析SQL类型
        SqlCommandType sqlType = handler.getMappedStatement().getSqlCommandType();

        if (isSelect(sqlType) && shouldAddFilter(sql)) {
            sql = addTenantFilter(sql);
            FieldUtils.setFieldValue(boundSql, "sql", sql);
        }

        return invocation.proceed();
    }

    private boolean shouldAddFilter(String sql) {
        // 简单判断：SELECT语句且不包含tenant_id过滤
        return sql.toLowerCase().contains("select")
            && !sql.toLowerCase().contains("tenant_id");
    }

    private String addTenantFilter(String sql) {
        String tenantId = tenantContext.getCurrentTenant();
        if (tenantId == null) {
            return sql;
        }

        // 简单的SQL注入防护：在WHERE后添加租户过滤
        // 实际生产环境应使用更严谨的SQL解析
        if (sql.toLowerCase().contains("where")) {
            return sql + " AND tenant_id = '" + tenantId + "'";
        } else {
            return sql + " WHERE tenant_id = '" + tenantId + "'";
        }
    }
}