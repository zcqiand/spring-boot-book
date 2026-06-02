@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<String> getAllUsers() {
        return List.of("Alice", "Bob", "Charlie");
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String getCurrentUser() {
        return "当前用户信息";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('USER_CREATE')")
    public String createUser() {
        return "用户创建成功";
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.annotation.Secured("ROLE_ADMIN")
    public String deleteUser(@PathVariable Long id) {
        return "用户 " + id + " 已删除";
    }

    @PutMapping("/password")
    @PreAuthorize("#username == authentication.name or hasRole('ADMIN')")
    public String updatePassword(@RequestParam String username,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword) {
        return "密码已更新";
    }
}