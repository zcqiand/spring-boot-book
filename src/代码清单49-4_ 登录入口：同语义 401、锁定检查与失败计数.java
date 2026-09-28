// 前略：package、两条 @impl 锚点注释、import 区与类注释，见源文件

@RestController
public class AuthController implements AuthApi {

  static final int LOCKOUT_THRESHOLD = 5;
  static final int LOCKOUT_MINUTES = 15;

// 中略：七个注入字段与构造器，见源文件

  @Override
  public ResponseEntity<LoginResponse> sessionsLogin(LoginRequest body) {
    // 2026-09-12 live 4-way 修复（R3 附带）：未知用户与错密码同语义 401 INVALID_CREDENTIALS
    // （msw oracle 口径，也不泄露用户存在性），此前 NSEE → 404 分叉。
    SysUser user =
        users
            .findByUsername(body.getUsername())
            .orElseThrow(saas.identity.platform.security.InvalidCredentialsException::new);

    // M01.F04.I02 — 锁定窗口检查
    if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(OffsetDateTime.now())) {
      LockedAccountResponse locked = new LockedAccountResponse();
      locked.setCode("ACCOUNT_LOCKED");
      locked.setMessage("连续失败次数过多，请稍后再试");
      locked.setLockedUntil(user.getLockedUntil());
      return ResponseEntity.status(HttpStatus.LOCKED).body(null);
    }

    // 家族 dev 种子约定（nextjs seed-db.mjs）：password 列可存 "plain:{password}"
    // 占位（Phase 5；prod 换 argon2/bcrypt）。aspnetcore/nextjs 两侧已识别该前缀，
    // springboot 对齐，否则同一份种子三后端登录行为分叉（contract-test live 401）。
    boolean plainOk =
        user.getPassword() != null && user.getPassword().equals("plain:" + body.getPassword());
    if (!plainOk && !bcrypt.matches(body.getPassword(), user.getPassword())) {
      int attempts = (user.getFailedAttempts() == null ? 0 : user.getFailedAttempts()) + 1;
      user.setFailedAttempts(attempts);
      if (attempts >= LOCKOUT_THRESHOLD) {
        user.setLockedUntil(OffsetDateTime.now().plusMinutes(LOCKOUT_MINUTES));
      }
      users.save(user);
      throw new saas.identity.platform.security.InvalidCredentialsException();
    }

// 中略：成功路径与租户归属解析，见代码清单49-5