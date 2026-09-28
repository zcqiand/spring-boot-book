// 前略：package、两行 @impl 锚点注释与 import 区，见源文件

/** M00.F05 租户应用订阅。skeleton。 */
@RestController
@Transactional
public class TenantApplicationsController implements TenantApplicationsApi {

  private final TenantApplicationRepository apps;
  private final saas.identity.platform.repository.OauthClientRepository oauthClients;
  private final TenantGuard tenantGuard;

  public TenantApplicationsController(
      TenantApplicationRepository apps,
      saas.identity.platform.repository.OauthClientRepository oauthClients,
      TenantGuard tenantGuard) {
    this.apps = apps;
    this.oauthClients = oauthClients;
    this.tenantGuard = tenantGuard;
  }

  @Override
  public ResponseEntity<TenantApplicationsListTenantApplications200Response>
      tenantApplicationsListTenantApplications(String tenantId, Integer page, Integer pageSize) {
    tenantGuard.verifyPathTenant(tenantId);
    int p = page == null ? 0 : page;
    int ps = pageSize == null ? 20 : pageSize;
    UUID tenantUuid = UUID.fromString(tenantId);
    var pg = apps.findByTenantId(tenantUuid, PageRequest.of(p, ps));
    TenantApplicationsListTenantApplications200Response resp =
        new TenantApplicationsListTenantApplications200Response();
    resp.setItems(pg.getContent().stream().map(this::toDto).toList());
    resp.setTotal(pg.getTotalElements());
    resp.setPage(p);
    resp.setPageSize(ps);
    return ResponseEntity.ok(resp);
  }

  // 后略：订阅、更新与移除三个方法及 toDto，更新见代码清单48-5