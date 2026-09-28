// 前略：package、@impl 锚点注释、import 区与类注释，见源文件

@Component
public class JwtIssuer {

  private final MACSigner signer;
  private final String issuer;
  private final String audience;
  private final long ttlSeconds;

  // CT_CONSTRUCTOR_THROW 在 spotbugs-exclude.xml 集中屏蔽（Spring bean 生命周期）

  public JwtIssuer(
      @Value("${JWT_SIGNING_KEY:}") String signingKey,
      @Value("${JWT_ISSUER:}") String issuer,
      @Value("${JWT_AUDIENCE:}") String audience,
      @Value("${JWT_TTL_SECONDS:}") Long ttlSecondsRaw) {
    // ADR-0019：issuer/audience/ttl 缺失 throw,不允许 "saas-identity-platform" / 3600 字面兜底。
    if (signingKey == null || signingKey.isEmpty()) {
      throw new IllegalStateException("JWT_SIGNING_KEY env not configured (ADR-0019 禁字面默认值)");
    }
    if (signingKey.getBytes(StandardCharsets.UTF_8).length < 32) {
      throw new IllegalStateException(
          "JWT_SIGNING_KEY must be >=32 bytes for HS256 (got "
              + signingKey.getBytes(StandardCharsets.UTF_8).length
              + ")");
    }
// 中略：issuer/audience/ttl 三项缺失即抛的检查，见源文件
    MACSigner macSigner;
    try {
      macSigner = new MACSigner(signingKey.getBytes(StandardCharsets.UTF_8));
    } catch (JOSEException e) {
      throw new IllegalStateException("JWT_SIGNING_KEY invalid for HS256", e);
    }
    this.signer = macSigner;
    this.issuer = issuer;
    this.audience = audience;
    this.ttlSeconds = ttlSecondsRaw;
  }

// 后略：签发方法、测试 helper 与 refresh token 生成，签发见代码清单49-7