@Service
public class NotificationService {
    private MessageSender messageSender; // 可选依赖

    // Setter注入
    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}