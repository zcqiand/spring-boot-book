@Entity
@Table(name = "samples")
public class SampleEntity {

  @Id
  @Column(name = "id", length = 64, nullable = false)
  private String id;

  @Column(name = "receipt_id", length = 64, nullable = false)
  private String receiptId;

  @Column(name = "sample_code", length = 64, nullable = false)
  private String sampleCode;

  @Column(name = "sample_name")
  private String sampleName;

  // ……（中略：型号、规格、等级、牌号、生产单位、批次、数量、来样与取样日期等档案字段，见源文件）

  @Column(name = "tenant_id", length = 64, nullable = false)
  private String tenantId = "";

  @Column(name = "created_at", nullable = false)
  private String createdAt = "";

  @Column(name = "updated_at", nullable = false)
  private String updatedAt = "";

  // ……（中略：全部 getter/setter，见源文件）
}