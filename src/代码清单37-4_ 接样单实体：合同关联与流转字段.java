@Entity
@Table(name = "sample_receipts")
public class SampleReceiptEntity {

  @Id
  @Column(name = "id", length = 64, nullable = false)
  private String id;

  @Column(name = "contract_id", length = 64, nullable = false)
  private String contractId;

  @Column(name = "commission_code", length = 64, nullable = false)
  private String commissionCode;

  @Column(name = "commission_date", nullable = false)
  private String commissionDate;

  // ……（中略：收样人、样品来源、检测类别等登记字段，见源文件）

  @Convert(converter = FlowStatusConverter.class)
  @Column(name = "flow_status", nullable = false)
  private FlowStatus flowStatus;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "flow_history", columnDefinition = "jsonb", nullable = false)
  private String flowHistory = "[]";

  // ……（中略：报告字段、租户字段与存取方法等，见源文件）
}