@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private TokenValidationService tokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                            GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // 跳过公开接口
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // 获取Token
        String authHeader = exchange.getRequest()
            .getHeaders().getFirst("Authorization");

        if (StringUtils.isBlank(authHeader)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = extractToken(authHeader);
        AuthenticationResult result = tokenService.validateToken(token);

        if (!result.isSuccess()) {
            log.warn("Token验证失败: path={}, error={}",
                path, result.getError());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 将用户信息传递给后端服务
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
            .header("X-User-Id", result.getUserId().toString())
            .header("X-Tenant-Id", result.getTenantId())
            .build();

        return chain.filter(
            exchange.mutate().request(mutatedRequest).build()
        );
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/public/") ||
               path.startsWith("/auth/") ||
               path.equals("/health");
    }

    private String extractToken(String authHeader) {
        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}