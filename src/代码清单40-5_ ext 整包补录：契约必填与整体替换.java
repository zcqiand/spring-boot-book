  public Sample updateExt(String tenantId, String id, UpdateSampleExtRequest req) {
    // 5.89：契约 ext 必填（sample.tsp UpdateSampleExtRequest.ext 无 ?）——缺省 IAE→400
    // （GlobalExceptionHandler 家族约定），不再让 HashMap 构造吃 null 抛 NPE→500。
    if (req == null || req.getExt() == null) {
      throw new IllegalArgumentException("ext is required");
    }
    var entity =
        repo.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new NoSuchElementException("Sample not found: " + id));
    entity.setExt(new java.util.HashMap<>(req.getExt()));
    entity.setUpdatedAt(nowIso());
    return SampleMapper.toDto(repo.save(entity));
  }