  private List<FlowActionResult> actForStage(
      String tenantId, FlowActionRequest req, FlowStatus requiredStage) {
    requireOperator(req);
    List<FlowActionResult> results = new ArrayList<>();
    String operator = req.getOperator();
    String reason = req.getReason();
    for (String id : req.getIds()) {
      try {
        SampleReceiptEntity entity =
            repo.findByTenantIdAndId(tenantId, id)
                .orElseThrow(
                    () -> new java.util.NoSuchElementException("Receipt not found: " + id));
        FlowStatus current = entity.getFlowStatus();
        if (current != requiredStage) {
          results.add(err(id, "Stage mismatch: requires " + requiredStage + " but is " + current));
          continue;
        }
        FlowStatus target;
        switch (req.getAction()) {
          case SUBMIT:
            target = SUBMIT_NEXT.get(current);
            break;
          case RETURN:
            target = RETURN_PREV.get(current);
            break;
          case WITHDRAW:
            // WITHDRAW 仅在 RECEIVING 阶段自转移
            target = (current == FlowStatus.RECEIVING) ? FlowStatus.RECEIVING : null;
            break;
          default:
            target = null;
        }
        if (target == null) {
          results.add(err(id, "Invalid transition from " + current + " with " + req.getAction()));
          continue;
        }
        receiptService.transitionTo(
            tenantId, id, current, target, req.getAction(), operator, reason);
        results.add(ok(id, target));
      } catch (Exception e) {
        results.add(err(id, e.getMessage()));
      }
    }
    return results;
  }