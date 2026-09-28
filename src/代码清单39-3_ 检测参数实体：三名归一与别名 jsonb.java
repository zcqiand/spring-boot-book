/**
 * V008__init_inspection_dictionary.sql — 检测参数（M06.F03）。PK = code。平台级字典（无 tenant_id，per V012
 * 备注）。aliases 走 jsonb（List<String>）。
 */
@Entity
@Table(name = "inspection_parameters")
public class InspectionParameterEntity {

  @Id
  @Column(name = "code", length = 64, nullable = false)
  private String code;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "raw_name", nullable = false)
  private String rawName;

  @Column(name = "canonical_name", nullable = false)
  private String canonicalName;

  @Column(name = "method_text")
  private String methodText;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "aliases", columnDefinition = "jsonb", nullable = false)
  private String aliases = "[]";

  @Column(name = "unit")
  private String unit;

  @Convert(converter = InspectionParameterSourceTypeConverter.class)
  @Column(name = "source_type", nullable = false)
  private InspectionParameterSourceType sourceType = InspectionParameterSourceType.OFFICIAL;

  // ……（中略：sortOrder 与 createdAt/updatedAt 时间戳及全部 getter/setter，见源文件）
}