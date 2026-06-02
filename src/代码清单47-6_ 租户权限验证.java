@Component
public class TenantSecurityService {

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private TenantRepository tenantRepository;

    public boolean hasPermission(String userId, String resourceTenantId) {
        String currentTenantId = tenantContext.getCurrentTenant();

        // 跨租户访问需要特殊权限
        if (!currentTenantId.equals(resourceTenantId)) {
            User user = userRepository.findById(userId);
            return user != null && user.isSuperAdmin();
        }

        return true;
    }

    public void validateTenantAccess(String resourceTenantId) {
        String currentTenantId = tenantContext.getCurrentTenant();

        if (!currentTenantId.equals(resourceTenantId)) {
            User currentUser = userContext.getCurrentUser();
            if (currentUser == null || !currentUser.isSuperAdmin()) {
                throw new AccessDeniedException("无权访问该租户资源");
            }
        }
    }
}