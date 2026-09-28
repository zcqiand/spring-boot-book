  public SampleReceipt transitionTo(
      String tenantId,
      String id,
      FlowStatus from,
      FlowStatus to,
      FlowAction action,
      String operator,
      String reason) {
    var entity = getEntity(tenantId, id);
    if (entity.getFlowStatus() != from) {
      throw new IllegalStateException(
          "Receipt " + id + " not in expected stage " + from + " but " + entity.getFlowStatus());
    }
    entity.setFlowStatus(to);
    // 5.69 last_submitted_by 对齐（SSOT = lab-nextjs db-queries.ts:271-276）：
    // submit 写当前操作人（前端登录态 user.id ?? user.username）；withdraw 清空；
    // return 保留原值。archived audit 自转移按 submit 语义走（actArchived 传 SUBMIT）。
    if (action == FlowAction.SUBMIT) {
      entity.setLastSubmittedBy(operator);
    } else if (action == FlowAction.WITHDRAW) {
      entity.setLastSubmittedBy(null);
    }
    // 5.75 history 记 action 真值（SSOT = lab-nextjs db-queries.ts:256-259）：
    // return/withdraw 条目也必须写 wire 值 return/withdraw，不许字面量 "submit"
    // （FlowAction @JsonValue 即 wire 值）。assignReceipt 的 "M03.F02 任务分配" 写
    // "submit" 是真 submit 转移（receiving→assigning），不在本修复范围。
    entity.setFlowHistory(
        SampleReceiptMapper.appendHistory(
            entity.getFlowHistory(),
            action.getValue(),
            operator,
            from.getValue(),
            to.getValue(),
            reason));
    entity.setUpdatedAt(nowIso());
    return SampleReceiptMapper.toDto(repo.save(entity));
  }

  // ……（中略：列表、详情、创建、更新、删除、任务分配与历史查询方法，见源文件）