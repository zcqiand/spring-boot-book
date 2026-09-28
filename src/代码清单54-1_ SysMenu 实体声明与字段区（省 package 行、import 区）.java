/**
 * DB-First scaffold：sys_menu（ADR-0025）。 由 scripts/scaffold-entities.mjs 从 saas_dev 真库反推生成。
 * 手写业务逻辑（@PreUpdate、@Convert、@EntityListeners 等） 通过 extends SysMenu 叠加在
 * src/main/java/.../entity/SysMenu.java。
 *
 * <p>字段含义见 saas-identity-platform-shared/src/db/schema.ts sysMenu。
 */
@Entity
@Table(name = "sys_menu")
public class SysMenu {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", columnDefinition = "uuid", nullable = false)
  private UUID id;

  @Column(name = "client_id", columnDefinition = "character varying", nullable = false)
  private String clientId;

  @Column(name = "parent_id", columnDefinition = "uuid", nullable = false)
  private UUID parentId;

  @Column(name = "title", columnDefinition = "character varying", nullable = false)
  private String title;

  @Column(name = "type", columnDefinition = "smallint", nullable = false)
  private Short type;

  @Column(name = "path", columnDefinition = "character varying")
  private String path;

  @Column(name = "component", columnDefinition = "character varying")
  private String component;

  @Column(name = "perms", columnDefinition = "character varying")
  private String perms;

  @Column(name = "icon", columnDefinition = "character varying")
  private String icon;

  @Column(name = "sort_order", columnDefinition = "integer", nullable = false)
  private Integer sortOrder;

  @Column(name = "status", columnDefinition = "smallint", nullable = false)
  private Short status;

  @Column(name = "created_at", columnDefinition = "timestamptz", nullable = false)
  private OffsetDateTime createdAt;

  // 后略：getter/setter 区与类收尾，见源文件