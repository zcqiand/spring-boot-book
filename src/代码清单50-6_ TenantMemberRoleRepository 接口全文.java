// 前略：package 与 import 区，见源文件

public interface TenantMemberRoleRepository extends JpaRepository<TenantMemberRole, UUID> {
  List<TenantMemberRole> findByMemberId(UUID memberId);

  void deleteByMemberId(UUID memberId);

  /** M04.F04.I08 — 一次查一批 member 的所有 role bindings */
  @Query("SELECT r FROM TenantMemberRole r WHERE r.memberId IN :memberIds")
  List<TenantMemberRole> findByMemberIds(@Param("memberIds") Collection<UUID> memberIds);
}