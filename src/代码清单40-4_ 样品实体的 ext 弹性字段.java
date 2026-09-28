  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "ext", columnDefinition = "jsonb", nullable = false)
  private Map<String, String> ext = new java.util.HashMap<>();

  // ……（中略：样品编号、档案、租户与时间戳字段，见源文件）