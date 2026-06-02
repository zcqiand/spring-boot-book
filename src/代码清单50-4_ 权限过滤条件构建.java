@Service
@RequiredArgsConstructor
public class PermissionFilterBuilder {

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private UserRoleRepository roleRepository;

    /**
     * 构建权限过滤条件
     */
    public Criteria buildFilter(String resourceType, Long userId) {
        String tenantId = tenantContext.getCurrentTenant();
        Criteria criteria = Criteria.where("tenantId").is(tenantId);

        // 获取用户角色
        List<UserRole> roles = roleRepository.findByUserIdAndTenantId(userId, tenantId);

        // 基于角色添加额外过滤条件
        for (UserRole role : roles) {
            addRoleFilter(criteria, role);
        }

        return criteria;
    }

    private void addRoleFilter(Criteria criteria, UserRole role) {
        switch (role.getRoleCode()) {
            case "DEPT_ADMIN":
                // 部门管理员只能看本部门数据
                criteria.and("departmentId").is(role.getDepartmentId());
                break;
            case "LAB_ADMIN":
                // 实验室管理员只能看本实验室数据
                criteria.and("labId").is(role.getLabId());
                break;
            case "AUDITOR":
                // 审计员可以看所有数据（只需租户隔离）
                break;
            default:
                // 普通用户只能看自己的数据
                criteria.and("createdBy").is(role.getUserId());
        }
    }
}