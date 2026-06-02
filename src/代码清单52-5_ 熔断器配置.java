@Configuration
@RequiredArgsConstructor
public class Resilience4jConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            // 熔断器打开的失败率阈值
            .failureRateThreshold(50)
            // 慢调用率阈值
            .slowCallRateThreshold(80)
            // 慢调用持续时间阈值
            .slowCallDurationThreshold(Duration.ofSeconds(2))
            // 熔断器打开的等待时间
            .waitDurationInOpenState(Duration.ofMinutes(1))
            // 半熔断状态下的最大调用数
            .permittedNumberOfCallsInHalfOpenState(10)
            // 滑动窗口大小
            .slidingWindowSize(100)
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .build();

        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        RateLimiterConfig config = RateLimiterConfig.custom()
            // 限流时限刷新周期
            .limitRefreshPeriod(Duration.ofSeconds(1))
            // 限流时的时间窗口
            .timeoutDuration(Duration.ofSeconds(5))
            // 默认限制
            .limitForPeriod(100)
            .build();

        return RateLimiterRegistry.of(config);
    }
}