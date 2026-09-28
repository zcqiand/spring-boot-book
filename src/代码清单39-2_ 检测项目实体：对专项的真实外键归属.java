/**
 * V008__init_inspection_dictionary.sql — 检测项目（M06.F02）。PK = code。平台级字典（无 tenant_id，per V012 备注）。FK
 * inspection_specialty_code → inspection_specialties(code) ON DELETE RESTRICT（V008 约束）。
 */
@Entity
@Table(name = "inspection_objects")
public class InspectionObjectEntity {

  @Id
  @Column(name = "code", length = 64, nullable = false)
  private String code;

  @Column(name = "inspection_specialty_code", length = 64, nullable = false)
  private String inspectionSpecialtyCode;

  @Column(name = "source_project_no", nullable = false)
  private String sourceProjectNo;

  @Column(name = "source_project_name", nullable = false)
  private String sourceProjectName;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "is_optional_for_qualification", nullable = false)
  private Boolean isOptionalForQualification = false;

  // ……（中略：isOfficial/enabled/sortOrder 与 createdAt/updatedAt 时间戳，见源文件）

  // ……（中略：全部 getter/setter，见源文件）
}