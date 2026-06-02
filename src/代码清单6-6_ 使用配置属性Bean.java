@Service
public class PaymentService {
    private final AlipayProperties alipayProperties;

    public PaymentService(AlipayProperties alipayProperties) {
        this.alipayProperties = alipayProperties;
    }

    public void pay() {
        String appId = alipayProperties.getAppId(); // 类型安全
        // ...
    }
}