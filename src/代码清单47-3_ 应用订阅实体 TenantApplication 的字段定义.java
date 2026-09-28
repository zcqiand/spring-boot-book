@Entity
@Table(name = "tenant_application")
public class TenantApplication {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", columnDefinition = "uuid", nullable = false)
  private UUID id;

  @Column(name = "tenant_id", columnDefinition = "uuid", nullable = false)
  private UUID tenantId;

  @Column(name = "client_id", columnDefinition = "character varying", nullable = false)
  private String clientId;

  @Column(name = "status", columnDefinition = "smallint", nullable = false)
  private Short status;

  @Column(name = "expire_time", columnDefinition = "timestamptz")
  private OffsetDateTime expireTime;

  @Column(name = "created_at", columnDefinition = "timestamptz", nullable = false)
  private OffsetDateTime createdAt;