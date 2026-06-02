@Service
@RequiredArgsConstructor
@Slf4j
public class TokenValidationService {

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private PlatformUserRepository userRepository;

    /**
     * 验证并提取用户信息
     */
    public AuthenticationResult validateToken(String token) {
        try {
            // 解析JWT（简化版，实际应使用专业JWT库）
            Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .parseClaimsJws(token)
                .getBody();

            String globalUserId = claims.getSubject();
            String tenantId = claims.get("tenant_id", String.class);

            PlatformUser user = userRepository.findByGlobalUserId(globalUserId);
            if (user == null) {
                return AuthenticationResult.fail("用户不存在");
            }

            if (user.getStatus() != UserStatus.ACTIVE) {
                return AuthenticationResult.fail("用户已被禁用");
            }

            // 设置租户上下文
            if (tenantId != null) {
                tenantContext.setCurrentTenant(tenantId);
            }

            return AuthenticationResult.success(user, tenantId);

        } catch (ExpiredJwtException e) {
            log.warn("Token已过期");
            return AuthenticationResult.fail("Token已过期");
        } catch (JwtException e) {
            log.warn("Token验证失败", e);
            return AuthenticationResult.fail("Token无效");
        }
    }
}