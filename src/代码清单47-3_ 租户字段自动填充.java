@Component
public class TenantFieldInterceptor {

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private EntityManager entityManager;

    @PrePersist
    public void prePersist(Object entity) {
        setTenantField(entity, "create");
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        setTenantField(entity, "update");
    }

    private void setTenantField(Object entity, String operation) {
        try {
            Field tenantField = entity.getClass().getDeclaredField("tenantId");
            tenantField.setAccessible(true);

            if (operation.equals("create") && tenantField.get(entity) == null) {
                tenantField.set(entity, tenantContext.getCurrentTenant());
            }
        } catch (NoSuchFieldException e) {
            // 实体没有租户字段，跳过
        } catch (IllegalAccessException e) {
            throw new RuntimeException("设置租户字段失败", e);
        }
    }
}