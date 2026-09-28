// 前略：package、@impl 锚点注释与 import 区，见源文件
/**
 * V008__init_inspection_dictionary.sql — 项目↔参数 junction（M06.F02/F03）。PK = (object_code,
 * parameter_code)。qualification_level 是 PG enum（QUALIFIED/RESTRICTED），用 {@link Enumerated#STRING}
 * 写常量名（与 PG enum 标签同款大写）。
 */
@Entity
@Table(name = "inspection_object_parameters")
@IdClass(ObjectParameterKey.class)
public class InspectionObjectParameterEntity {

  @Id
  @Column(name = "inspection_object_code", length = 64, nullable = false)
  private String inspectionObjectCode;

  @Id
  @Column(name = "inspection_parameter_code", length = 64, nullable = false)
  private String inspectionParameterCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "qualification_level", nullable = false)
  private QualificationLevel qualificationLevel = QualificationLevel.QUALIFIED;

  @Column(name = "source_page")
  private Integer sourcePage;

  @Column(name = "remark")
  private String remark;

  @Column(name = "created_at", nullable = false)
  private String createdAt = "";

  @Column(name = "updated_at", nullable = false)
  private String updatedAt = "";

  // 中略：全部 getter 与 setter，见源文件
}