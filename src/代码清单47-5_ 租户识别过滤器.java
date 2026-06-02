@Component
@Slf4j
public class TenantIdentificationFilter extends OncePerRequestFilter {

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private TenantConfigService tenantConfigService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String tenantId = resolveTenantId(request);

        if (tenantId != null && tenantConfigService.isValidTenant(tenantId)) {
            tenantContext.setCurrentTenant(tenantId);
            log.debug("请求识别到租户: {}", tenantId);
        } else if (tenantId != null) {
            log.warn("无效的租户ID: {}", tenantId);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的租户ID");
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            tenantContext.setCurrentTenant(null); // 请求结束清理
        }
    }

    private String resolveTenantId(HttpServletRequest request) {
        // 1. 优先从请求头获取
        String tenantId = request.getHeader("X-Tenant-ID");
        if (StringUtils.isNotBlank(tenantId)) {
            return tenantId;
        }

        // 2. 从域名提取子域名
        String host = request.getServerName();
        String[] parts = host.split("\\.");
        if (parts.length >= 3) {
            return parts[0]; // labA.example.com -> labA
        }

        // 3. 从URL路径提取
        String uri = request.getRequestURI();
        String[] pathParts = uri.split("/");
        if (pathParts.length >= 2) {
            // /api/v1/tasks -> v1是版本，/tenantA/api/v1/tasks -> tenantA是租户
            String firstPath = pathParts[1];
            if (!firstPath.equals("api") && tenantConfigService.isValidTenant(firstPath)) {
                return firstPath;
            }
        }

        return null;
    }
}