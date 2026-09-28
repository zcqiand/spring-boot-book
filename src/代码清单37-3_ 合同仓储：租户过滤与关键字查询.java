public interface ContractRepository extends JpaRepository<ContractEntity, String> {

  // status 过滤用 IS NULL 判断而不是 = ''：status 为 null 时 `null = ''` 在 SQL 里
  // 是 UNKNOWN（非 TRUE），整个 WHERE 折叠为空 → 列表恒空（真库踩坑，mock 测试测不出）。
  @Query(
      "SELECT c FROM ContractEntity c WHERE c.tenantId = :tenantId"
          + " AND (:keyword = '' OR LOWER(c.contractCode) LIKE LOWER(CONCAT('%', :keyword, '%'))"
          + "   OR LOWER(c.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')))"
          + " AND (:status IS NULL OR c.status = :status)"
          + " ORDER BY c.updatedAt DESC, c.contractCode")
  List<ContractEntity> filter(
      @Param("tenantId") String tenantId,
      @Param("keyword") String keyword,
      @Param("status") ContractStatus status);

  Optional<ContractEntity> findByTenantIdAndId(String tenantId, String id);
}