// 前略：package、@impl 锚点注释与 import 区，见源文件
/**
 * V010__init_param_interfaces.sql — 参数界面（M06.F08）。PK = code。平台级字典（无 tenant_id，per V012 备注）。 config
 * 是 jsonb（Map<String,Object>），写库时序列化 JSON 字符串。
 */
@Entity
@Table(name = "inspection_param_interfaces")
public class ParamInterfaceEntity {

  @Id
  @Column(name = "code", length = 64, nullable = false)
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "component_path", nullable = false)
  private String componentPath;

  @Column(name = "description")
  private String description;

  @Column(name = "is_official")
  private Boolean isOfficial;

  @Column(name = "sort_order", nullable = false)
  private Integer sortOrder = 0;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "config", columnDefinition = "jsonb")
  private String config;

  @Column(name = "created_at", nullable = false)
  private String createdAt = "";

  @Column(name = "updated_at", nullable = false)
  private String updatedAt = "";

  // 中略：全部 getter 与 setter，见源文件
}