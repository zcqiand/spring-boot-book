@Entity
@Table(name = "social_users")
public class SocialUser {

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
    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    // Getter和Setter省略
}