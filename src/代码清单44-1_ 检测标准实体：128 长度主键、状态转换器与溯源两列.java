// 前略：package、@impl 锚点注释与 import 区，见源文件
/**
 * V008__init_inspection_dictionary.sql — 检测标准（M06.F04）。PK = code。平台级字典（无 tenant_id，per V012
 * 备注）。code 可含 "/"（per SQL 注释）。
 */
@Entity
@Table(name = "inspection_standards")
public class InspectionStandardEntity {

  @Id
  @Column(name = "code", length = 128, nullable = false)
  private String code;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "version")
  private String version;

  @Convert(converter = InspectionStandardStatusConverter.class)
  @Column(name = "status", nullable = false)
  private InspectionStandardStatus status = InspectionStandardStatus.ACTIVE;

  @Column(name = "source_document_id")
  private String sourceDocumentId;

  @Column(name = "source_hash")
  private String sourceHash;

  @Column(name = "sort_order", nullable = false)
  private Integer sortOrder = 0;

  @Column(name = "created_at", nullable = false)
  private String createdAt = "";

  @Column(name = "updated_at", nullable = false)
  private String updatedAt = "";

  // 中略：全部 getter 与 setter，见源文件
}