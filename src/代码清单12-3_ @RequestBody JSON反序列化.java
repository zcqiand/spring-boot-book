@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * @RequestBody会自动：
     * 1. 根据Content-Type（必须是application/json）选择消息转换器
     * 2. 调用ObjectMapper将JSON反序列化为User对象
     * 3. 如果反序列化失败，返回400 Bad Request
     */
    @PostMapping
    public String createUser(@RequestBody User user) {
        return "创建用户: " + user.getUsername();
    }
}

class User {
    private String username;
    private String email;
    // getter/setter
}