@RestController
public class UserController {
    // 正确方式：使用SLF4J接口
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
}