public interface SampleRepository extends JpaRepository<SampleEntity, String> {

  @Query(
      "SELECT s FROM SampleEntity s"
          + " WHERE (:tenantId = '' OR s.tenantId = :tenantId)"
          + " AND (:receiptId = '' OR s.receiptId = :receiptId)"
          + " AND (:keyword = '' OR LOWER(s.sampleCode) LIKE LOWER(CONCAT('%', :keyword, '%'))"
          + "   OR LOWER(s.sampleName) LIKE LOWER(CONCAT('%', :keyword, '%')))"
          + " ORDER BY s.createdAt DESC, s.sampleCode")
  List<SampleEntity> filter(
      @Param("tenantId") String tenantId,
      @Param("receiptId") String receiptId,
      @Param("keyword") String keyword);

  Optional<SampleEntity> findByTenantIdAndId(String tenantId, String id);
}