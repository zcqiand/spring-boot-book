@Component
@RequiredArgsConstructor
public class CrossTenantAccessController {

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private UserPermissionService permissionService;

    /**
     * 检查是否有跨租户访问权限
     */
    public boolean canAccessTenant(Long userId, String targetTenantId) {
        PlatformUser user = platformUserRepository.findById(userId)
            .orElse(null);

        if (user == null) {
            return false;
        }

        // 平台管理员可以访问所有租户
        if (user.isPlatformAdmin()) {
            return true;
        }

        // 租户管理员可以访问关联租户
        List<String> accessibleTenants = tenantUserMappingRepository
            .findByPlatformUserId(userId).stream()
            .filter(m -> m.getDefaultRole().isAdmin())
            .map(TenantUserMapping::getTenantId)
            .collect(Collectors.toList());

        return accessibleTenants.contains(targetTenantId);
    }

    /**
     * 切换租户上下文
     */
    public void switchTenantContext(String targetTenantId, Long userId) {
        if (!canAccessTenant(userId, targetTenantId)) {
            throw new AccessDeniedException("无权访问该租户");
        }

        tenantContext.setCurrentTenant(targetTenantId);
        SecurityContextHolder.getContext().setAuthentication(
            new TenantAuthentication(
                userId,
                targetTenantId,
                buildTenantAuthorities(userId, targetTenantId)
            )
        );
    }
}