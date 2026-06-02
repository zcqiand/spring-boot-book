@Service
public class PaymentService {
    @Value("${payment.alipay.app-id}")
    private String alipayAppId;

    @Value("${payment.alipay.private-key}")
    private String alipayPrivateKey;

    @Value("${payment.alipay.public-key}")
    private String alipayPublicKey;

    @Value("${payment.wechat.app-id}")
    private String wechatAppId;

    @Value("${payment.wechat.mch-id}")
    private String wechatMchId;
    // ... 10个、20个配置项
}