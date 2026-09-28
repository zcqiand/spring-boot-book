/**
 * V008__init_inspection_dictionary.sql — 检测专项（M06.F01）。PK = code。平台级字典（无 tenant_id，per V012 备注）。
 */
@Entity
@Table(name = "inspection_specialties")
public class InspectionSpecialtyEntity {

  @Id
  @Column(name = "code", length = 64, nullable = false)
  private String code;

  @Column(name = "official_no", nullable = false)
  private String officialNo;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "is_official", nullable = false)
  private Boolean isOfficial = true;

  @Column(name = "enabled", nullable = false)
  private Boolean enabled = true;

  @Column(name = "sort_order", nullable = false)
  private Integer sortOrder = 0;

  @Column(name = "created_at", nullable = false)
  private String createdAt = "";

  @Column(name = "updated_at", nullable = false)
  private String updatedAt = "";

  // ……（中略：全部 getter/setter，见源文件）
}