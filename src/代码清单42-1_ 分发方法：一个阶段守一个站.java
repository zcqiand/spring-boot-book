  /** M03.F01.I08/I09/I10 接样阶段 act — 允许 SUBMIT/RETURN/WITHDRAW。 */
  public List<FlowActionResult> actReceiving(String tenantId, FlowActionRequest req) {
    return actForStage(tenantId, req, FlowStatus.RECEIVING);
  }

  // ……（中略：actAssigning、actDataEntry 两个同款分发方法，见源文件）

  /** M03.F05.I07/I08/I09 报告审核阶段 act — 允许 SUBMIT/RETURN。 */
  public List<FlowActionResult> actReview(String tenantId, FlowActionRequest req) {
    return actForStage(tenantId, req, FlowStatus.REVIEW);
  }

  // ……（中略：actApprove、actIssuance、actArchived 三个方法，见源文件）