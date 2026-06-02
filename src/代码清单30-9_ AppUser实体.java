@Entity
@Table(name = "app_users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(unique = true)
    private String email;

    @Column
    private String nickname;

    @Column
    private String avatarUrl;

    @Column
    private String role = "USER";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt = LocalDateTime.now();

    // Getter和Setter省略
}