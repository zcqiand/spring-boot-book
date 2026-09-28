// 前略：package、@impl 锚点注释与 import 区，见源文件

/**
 * Holds the current tenant_id from the authenticated JWT. Used by TenantGuard to verify
 * path-carried tenantId matches JWT claim.
 */
@Component
public class TenantContext {
  public String currentTenantId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) return null;
    return jwt.getClaimAsString("tenant_id");
  }

  public String currentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) return null;
    return jwt.getSubject();
  }
}