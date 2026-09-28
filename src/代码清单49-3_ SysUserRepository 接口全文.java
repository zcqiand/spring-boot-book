// 前略：package 与 import 区，见源文件

public interface SysUserRepository extends JpaRepository<SysUser, UUID> {
  Optional<SysUser> findByUsername(String username);

  Optional<SysUser> findByEmail(String email);
}