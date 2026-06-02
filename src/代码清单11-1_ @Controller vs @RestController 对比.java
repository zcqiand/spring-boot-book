// 传统Controller：返回视图名，由视图解析器渲染HTML
@Controller
@RequestMapping("/users")
public class UserController {
    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";  // 返回视图名，Spring会渲染user-list.jsp/thymeleaf
    }
}

// RestController：返回数据，直接写入HTTP响应体
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    @GetMapping("/list")
    public List<User> listUsers() {
        return userService.findAll();  // 直接序列化为JSON
    }
}