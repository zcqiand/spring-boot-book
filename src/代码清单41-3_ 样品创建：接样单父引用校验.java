  public Sample create(String tenantId, CreateSampleRequest req) {
    if (!receiptRepo.findByTenantIdAndId(tenantId, req.getReceiptId()).isPresent()) {
      throw new NoSuchElementException("Receipt not found: " + req.getReceiptId());
    }
    String now = nowIso();
    return SampleMapper.toDto(repo.save(SampleMapper.fromCreate(req, newId(), tenantId, now)));
  }

  // ……（中略：列表、详情、更新、ext 补录与删除方法，见源文件）