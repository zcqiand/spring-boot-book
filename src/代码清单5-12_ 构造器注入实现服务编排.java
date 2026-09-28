// 第一步：定义抽象接口
public interface MessageSender {
    void send(String message);
}

// 第二步：创建实现类
@Service
public class EmailSender implements MessageSender {
    @Override
    public void send(String message) {
        System.out.println("发送邮件: " + message);
    }
}

// 第三步：创建依赖方，使用构造器注入
@Service
public class NotificationService {
    private final MessageSender messageSender; // 依赖不可变

    // 构造器注入：Spring自动传入
    public NotificationService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void notifyUser(String message) {
        messageSender.send(message); // 使用注入的依赖
    }
}

// 第四步：验证注入
@SpringBootTest
public class ConstructorInjectionTest {
    @Autowired
    private NotificationService notificationService;

    @Test
    void shouldInjectMessageSender() {
        assertNotNull(notificationService);
        assertNotNull(notificationService.getClass().getDeclaredField("messageSender"));
    }
}