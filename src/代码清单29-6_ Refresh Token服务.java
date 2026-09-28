@Service
public class RefreshTokenService {

    // 生产环境应使用Redis存储，这里用内存模拟
    private final ConcurrentHashMap<String, Long> refreshTokenStore = new ConcurrentHashMap<>();

    // 存储Refresh Token对应的用户名
    public void storeRefreshToken(String refreshToken, String username) {
        refreshTokenStore.put(refreshToken, System.currentTimeMillis());
    }

    // 验证Refresh Token是否有效
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = jwtService.parseToken(refreshToken);
            String type = claims.get("type", String.class);

            // 必须确实是refresh类型
            if (!"refresh".equals(type)) {
                return false;
            }

            // 检查是否在有效期内
            return !claims.getExpiration().before(new java.util.Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 使用Refresh Token获取新的Access Token
    public String refreshAccessToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh Token无效或已过期");
        }

        Claims claims = jwtService.parseToken(refreshToken);
        String username = claims.getSubject();
        String roles = claims.get("roles", String.class);

        // 创建新的Access Token
        return jwtService.createAccessToken(username, roles);
    }

    // 吊销Refresh Token（登出时调用）
    public void revokeRefreshToken(String refreshToken) {
        refreshTokenStore.remove(refreshToken);
    }
}