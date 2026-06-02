@Component
@ConfigurationProperties(prefix = "payment.alipay")
public class AlipayProperties {
    private String appId;
    private String privateKey;
    private String publicKey;

    // getter和setter自动生成
}