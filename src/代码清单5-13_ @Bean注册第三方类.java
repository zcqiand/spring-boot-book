// 假设这是来自第三方的类（无法修改其源码）
public class HttpClient {
    private final String baseUrl;

    public HttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String get(String path) {
        return "Response from " + baseUrl + path;
    }
}

// 使用@Bean将其注册为Spring Bean
@Configuration
public class HttpClientConfig {
    @Bean
    public HttpClient httpClient() {
        // 可以精细控制创建过程
        return new HttpClient("https://api.example.com");
    }
}

// 在Service中使用
@Service
public class ApiService {
    private final HttpClient httpClient;

    public ApiService(HttpClient httpClient) {
        this.httpClient = httpClient; // 构造器注入
    }
}