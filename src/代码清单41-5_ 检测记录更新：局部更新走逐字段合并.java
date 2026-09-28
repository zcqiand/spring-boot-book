  public TestRecord update(String tenantId, String id, UpdateTestRecordRequest req) {
    TestRecordEntity entity =
        repo.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new NoSuchElementException("TestRecord not found: " + id));
    TestRecordMapper.applyUpdate(entity, req, nowIso());
    return TestRecordMapper.toDto(repo.save(entity));
  }

  // ……（中略：删除与改判方法，见源文件）