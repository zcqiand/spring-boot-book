  // 中略：仪表盘统计方法、材料类型映射与计数辅助方法，见源文件
  private static Map<String, String> renderRow(SampleReceiptEntity r) {
    Map<String, String> row = new LinkedHashMap<>();
    row.put("commissionCode", r.getCommissionCode() == null ? "" : r.getCommissionCode());
    row.put("categoryCode", r.getCategoryCode() == null ? "" : r.getCategoryCode());
    row.put("projectName", r.getProjectName() == null ? "" : r.getProjectName());
    row.put("flowStatus", r.getFlowStatus() == null ? "" : r.getFlowStatus().getValue());
    row.put("result", r.getResult() == null ? "" : r.getResult().getValue());
    row.put("reportCode", r.getReportCode() == null ? "" : r.getReportCode());
    return row;
  }