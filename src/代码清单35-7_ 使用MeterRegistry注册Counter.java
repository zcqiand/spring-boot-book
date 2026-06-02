@Service
public class OrderService {
    private final Counter orderCreatedCounter;
    private final Counter orderFailedCounter;

    public OrderService(MeterRegistry meterRegistry) {
        this.orderCreatedCounter = Counter.builder("business.order.created")
                .description("订单创建总数")
                .tag("service", "order-service")
                .register(meterRegistry);
        this.orderFailedCounter = Counter.builder("business.order.failed")
                .description("订单创建失败总数")
                .tag("service", "order-service")
                .register(meterRegistry);
    }

    public void createOrder(Order order) {
        try {
            orderRepository.save(order);
            orderCreatedCounter.increment();
        } catch (Exception e) {
            orderFailedCounter.increment();
            throw e;
        }
    }
}