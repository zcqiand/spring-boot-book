// 前略：package、@impl 锚点注释与 import 区，见源文件
public interface OauthCodeRepository extends JpaRepository<OauthCode, UUID> {
  Optional<OauthCode> findByCode(String code);
}