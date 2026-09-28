// 前略：package、@impl 锚点注释与 import 区，见源文件
public interface OauthRefreshTokenRepository extends JpaRepository<OauthRefreshToken, UUID> {
  Optional<OauthRefreshToken> findByRefreshToken(String refreshToken);
}