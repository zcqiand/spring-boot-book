@Entity
@Table(name = "contracts")
public class ContractEntity {

  @Id
  @Column(name = "id", length = 64, nullable = false)
  private String id;

  @Column(name = "contract_code", length = 64, nullable = false)
  private String contractCode;

  @Column(name = "client_unit", nullable = false)
  private String clientUnit;

  @Column(name = "project_name", nullable = false)
  private String projectName;

  @Column(name = "construction_unit", nullable = false)
  private String constructionUnit;

  // ……（中略：监理单位、见证单位、见证人、联系人等参与方字段，见源文件）

  @Convert(converter = ContractStatusConverter.class)
  @Column(name = "status", nullable = false)
  private ContractStatus status;

  @Column(name = "tenant_id", length = 64, nullable = false)
  private String tenantId = "";

  @Column(name = "created_at", nullable = false)
  private String createdAt = "";

  @Column(name = "updated_at", nullable = false)
  private String updatedAt = "";

  // ……（中略：全部 getter/setter，见源文件）
}