@Configuration
@RequiredArgsConstructor
public class ServiceProxyConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .failureRateThreshold(50)        // 熔断器打开的失败率阈值
            .slowCallRateThreshold(80)       // 慢调用率阈值
            .slowCallDurationThreshold(Duration.ofSeconds(3))
            .waitDurationInOpenState(Duration.ofMinutes(1))
            .permittedNumberOfCallsInHalfOpenState(5)
            .slidingWindowSize(10)
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .build();

        return CircuitBreakerRegistry.of(config);
    }

    // 为远程服务创建代理，添加熔断能力
    @Bean
    public TaskService taskServiceProxy(CircuitBreakerRegistry registry) {
        CircuitBreaker taskServiceBreaker = registry.circuitBreaker("task-service");

        return (TaskService) ProxyBuilder
            .on(new TaskServiceClient())
            .around(taskServiceBreaker)
            .get();
    }
}