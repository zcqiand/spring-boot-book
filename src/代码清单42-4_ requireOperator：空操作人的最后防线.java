  /**
   * 5.75 operator 契约必填边缘对齐（SSOT = lab-nextjs act-route.ts:39-44）：缺失与空串都 400。 null 已由
   * FlowActionRequest @NotNull 在 controller 层 400 拦截；空串在此拦截 → GlobalExceptionHandler 400
   * {code:"BAD_REQUEST", message:"operator is required"}。 校验先于 per-id 循环（整批拒，与 nextjs act-route
   * 顺序一致）。
   */
  private static void requireOperator(FlowActionRequest req) {
    String operator = req.getOperator();
    if (operator == null || operator.isEmpty()) {
      throw new IllegalArgumentException("operator is required");
    }
  }