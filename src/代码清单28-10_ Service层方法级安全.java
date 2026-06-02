@Service
public class BusinessService {

    @PreAuthorize("permitAll()")
    public String getPublicInfo() {
        return "公开信息";
    }

    @PreAuthorize("isAuthenticated()")
    public String getPrivateInfo() {
        return "私有信息";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void adminOperation() {
        // 管理员操作
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == authentication.principal.id)")
    public String getUserData(Long id) {
        return "数据内容";
    }
}