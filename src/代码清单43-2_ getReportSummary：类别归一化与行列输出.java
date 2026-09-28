  // 中略：MATERIAL_KEYWORDS 材料码表、四个仓储字段与构造器，见源文件
  /**
   * 报告汇总：按 categoryCode（ALL = 全表）过滤当前租户的接样单，输出列 + 行。
   *
   * <p>dateFrom/dateTo 简化为 commissionDate 前缀匹配（YYYY-MM-DD 字符串字典序与日期序一致）。 null 视作无界。
   */
  public SummaryData getReportSummary(
      String tenantId, String categoryCode, String dateFrom, String dateTo) {
    String cat = categoryCode == null || categoryCode.isBlank() ? CATEGORY_ALL : categoryCode;
    String from = dateFrom == null ? "" : dateFrom;
    String to = dateTo == null ? "" : dateTo;

    List<SampleReceiptEntity> rows = receiptRepo.summary(tenantId, cat, from, to);

    List<Map<String, String>> rendered = rows.stream().map(SummaryService::renderRow).toList();
    return new SummaryData()
        .summaryName("报告汇总（" + cat + "）")
        .columns(SUMMARY_COLUMNS)
        .rows(rendered);
  }