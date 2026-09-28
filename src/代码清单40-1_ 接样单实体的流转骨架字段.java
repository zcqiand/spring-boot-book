  @Convert(converter = FlowStatusConverter.class)
  @Column(name = "flow_status", nullable = false)
  private FlowStatus flowStatus;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "flow_history", columnDefinition = "jsonb", nullable = false)
  private String flowHistory = "[]";

  @Column(name = "last_submitted_by")
  private String lastSubmittedBy;

  @Column(name = "assignee_id")
  private String assigneeId;

  @Column(name = "assignee_name")
  private String assigneeName;

  @Column(name = "planned_test_date")
  private String plannedTestDate;

  // ……（中略：登记、报告、租户与时间戳字段及全部存取方法，见源文件）