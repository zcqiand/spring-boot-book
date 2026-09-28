  public TestRecord create(String tenantId, CreateTestRecordRequest req) {
    String now = nowIso();
    return TestRecordMapper.toDto(
        repo.save(TestRecordMapper.fromCreate(req, newId(), tenantId, now)));
  }

  // ……（中略：列表、详情、更新、删除方法，见源文件）