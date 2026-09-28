  /** SUBMIT（推进）阶段映射。 */
  private static final Map<FlowStatus, FlowStatus> SUBMIT_NEXT = initNext();

  /** RETURN（退回上一阶段）映射。 */
  private static final Map<FlowStatus, FlowStatus> RETURN_PREV = initPrev();

  private static Map<FlowStatus, FlowStatus> initNext() {
    EnumMap<FlowStatus, FlowStatus> m = new EnumMap<>(FlowStatus.class);
    m.put(FlowStatus.RECEIVING, FlowStatus.TASK_ASSIGNMENT);
    m.put(FlowStatus.TASK_ASSIGNMENT, FlowStatus.DATA_ENTRY);
    m.put(FlowStatus.DATA_ENTRY, FlowStatus.REVIEW);
    m.put(FlowStatus.REVIEW, FlowStatus.APPROVAL);
    m.put(FlowStatus.APPROVAL, FlowStatus.ISSUANCE);
    m.put(FlowStatus.ISSUANCE, FlowStatus.ARCHIVED);
    return java.util.Collections.unmodifiableMap(m);
  }

  // ……（中略：initPrev 按原路逐站退回，receiving 无上一站、archived 退回 issuance，见源文件）