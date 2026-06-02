@Entity
@Table(name = "audit_log", indexes = {
    @Index(name = "idx_audit_time", columnList = "timestamp"),
    @Index(name = "idx_audit_user", columnList = "userId"),
    @Index(name = "idx_audit_tenant", columnList = "tenantId"),
    @Index(name = "idx_audit_action", columnList = "actionType")
})
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, length = 50)
    private String actionType; // CREATE/UPDATE/DELETE/READ/LOGIN/LOGOUT

    @Column(nullable = false, length = 100)
    private String resourceType; // 资源类型，如 TASK, REPORT, USER

    @Column(length = 100)
    private String resourceId; // 资源ID

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 200)
    private String username; // 冗余存储，便于查询

    @Column(length = 50)
    private String userRole; // 操作时的角色

    @Column(columnDefinition = "TEXT")
    private String requestInfo; // 请求信息（脱敏后）

    @Column(columnDefinition = "TEXT")
    private String responseInfo; // 响应信息（脱敏后）

    @Column(length = 50)
    private String clientIp; // 客户端IP

    @Column(length = 500)
    private String userAgent; // 浏览器标识

    @Column(length = 20)
    private String result; // SUCCESS/FAILURE

    @Column(columnDefinition = "TEXT")
    private String errorMessage; // 错误信息（如有）

    @Column
    private Long durationMs; // 操作耗时（毫秒）
}