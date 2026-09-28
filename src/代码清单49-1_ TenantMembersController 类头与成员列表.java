// 前略：package、两条 @impl 锚点注释与 import 区，见源文件

/**
 * M00.F02 租户成员 CRUD + 邀请 + 状态切换 + M01.F02 角色绑定。
 *
 * <p>ADR-0032（2026-09-12）：成员 list/create/get/patch/put-roles/patch-status 六端点改扁平 {@link
 * TenantMemberUserView}，路径参数 {userId} 语义 = sys_user.id（经 tenant_member.tenant_id + user_id 寻址，不再是
 * tenant_member.id）。invitations 端点保持嵌套 TenantMemberView 不动（同家族裁决）。
 */
@RestController
@Transactional
public class TenantMembersController implements TenantMembersApi {

  private final TenantMemberRepository members;
  private final TenantMemberRoleRepository memberRoles;
  private final SysUserRepository users;
  private final saas.identity.platform.repository.SysRoleRepository sysRoles;
  private final TenantGuard tenantGuard;
  private final MemberViewAssembler assembler;

// 中略：构造器，见源文件

  @Override
  public ResponseEntity<TenantMembersListTenantUsers200Response> tenantMembersListTenantUsers(
      String tenantId, Integer page, Integer pageSize, TenantMemberStatus status) {
    tenantGuard.verifyPathTenant(tenantId);
    int p = page == null ? 0 : page;
    int ps = pageSize == null ? 20 : pageSize;
    UUID tenantUuid = UUID.fromString(tenantId);
    // status query 参数过滤（ADR-0032）：分页前 DB 级执行（S2 修复，对齐 aspnetcore），
    // total = 过滤后计数；视图 status 现读 member 行（S1），与过滤键天然一致。
    // 2026-09-13 排序对齐（积压清偿）：家族约定 list = created_at DESC
    // （nextjs ORDER BY created_at DESC / aspnetcore OrderByDescending(CreatedAt)
    // 早已实现，本仓 PageRequest 无 Sort 是漏网 —— 无排序时 PG 返回堆序，
    // 与两兄弟及 msw 镜像在运行期新建成员后必分叉）。
    // 2026-09-18 tiebreak 显式化：家族 seed 多行 created_at 相同，仅按 created_at 排序时
    // PG 返回堆序 → 顺序不稳定；tiebreak = id ASC（对齐 findByUserId 的 id ASC 先例）。
    var sort = Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.ASC, "id"));
    var pg =
        (status == null)
            ? members.findByTenantId(tenantUuid, PageRequest.of(p, ps, sort))
            : members.findByTenantIdAndStatus(
                tenantUuid, MemberStatusMapper.toDb(status), PageRequest.of(p, ps, sort));
    // 扁平视图顶层是 user 行：一页 member 对应一批 user，批量取避免 N+1。
    var userMap =
        users.findAllById(pg.getContent().stream().map(TenantMember::getUserId).toList()).stream()
            .collect(java.util.stream.Collectors.toMap(SysUser::getId, u -> u));
    List<TenantMemberUserView> items =
        pg.getContent().stream()
            .filter(m -> userMap.containsKey(m.getUserId()))
            .map(m -> toView(m, userMap.get(m.getUserId())))
            .toList();
    TenantMembersListTenantUsers200Response resp = new TenantMembersListTenantUsers200Response();
    resp.setItems(items);
    resp.setTotal(pg.getTotalElements());
    resp.setPage(p);
    resp.setPageSize(ps);
    return ResponseEntity.ok(resp);
  }

// 后略：创建、详情、更新、删除、状态切换与角色绑定方法，邀请见代码清单49-2