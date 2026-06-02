@Component
@RequiredArgsConstructor
@Slf4j
public class CircuitBreakerFilter implements GlobalFilter, Ordered {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private BackendService backendService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                            GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // 提取后端服务ID
        String backendId = extractBackendId(path);

        CircuitBreaker circuitBreaker = circuitBreakerRegistry
            .circuitBreaker(backendId);

        return circuitBreaker.executeMono(
                supplier -> backendService.callBackend(exchange)
            )
            .transform(r -> chain.filter(r))
            .onErrorResume(e -> {
                log.error("后端服务调用失败: backend={}, error={}",
                    backendId, e.getMessage());
                return createFallbackResponse(exchange, backendId);
            })
            .then();
    }

    private String extractBackendId(String path) {
        // 从路径提取服务ID，如 /api/lab/tasks -> lab
        if (path.startsWith("/api/")) {
            String[] parts = path.substring(5).split("/");
            return parts[0];
        }
        return "default";
    }

    private Mono<Void> createFallbackResponse(
            ServerWebExchange exchange, String backendId) {

        exchange.getResponse().setStatusCode(
            HttpStatus.SERVICE_UNAVAILABLE);
        exchange.getResponse().getHeaders().add(
            "X-Fallback", "true");

        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}