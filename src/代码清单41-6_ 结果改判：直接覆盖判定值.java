  /** 人工改判 verdict（M03.F05/F06 报告流程可能触发）。 */
  public TestRecord setVerdict(String tenantId, String id, String verdict) {
    TestRecordEntity entity =
        repo.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new NoSuchElementException("TestRecord not found: " + id));
    entity.setVerdict(verdict);
    entity.setUpdatedAt(nowIso());
    return TestRecordMapper.toDto(repo.save(entity));
  }