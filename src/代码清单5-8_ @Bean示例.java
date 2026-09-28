@Configuration
public class ThirdPartyConfig {
    @Bean
    public RestTemplate restTemplate() {
        // 创建一个RestTemplate实例，Spring会将其纳入管理
        return new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(10))
            .build();
    }
}