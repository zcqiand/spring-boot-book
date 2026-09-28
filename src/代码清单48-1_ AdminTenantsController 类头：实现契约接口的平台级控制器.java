// 前略：package、两行 @impl 锚点注释与 import 区，见源文件

/** M00.F01 平台 admin 租户 CRUD。skeleton。 */
@RestController
@Transactional
public class AdminTenantsController implements AdminTenantsApi {

  private final TenantRepository tenants;

  public AdminTenantsController(TenantRepository tenants) {
    this.tenants = tenants;
  }