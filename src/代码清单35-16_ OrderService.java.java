@Service
public class OrderService {
    private final Counter orderCreatedCounter;
    private final Counter orderFailedCounter;

    public OrderService(MeterRegistry meterRegistry) {
        this.orderCreatedCounter = Counter.builder("exercise.order.created")
                .tag("service", "order-service").register(meterRegistry);
        this.orderFailedCounter = Counter.builder("exercise.order.failed")
                .tag("service", "order-service").register(meterRegistry);
    }

    public Order createOrder(String customerId, BigDecimal amount) {
        try {
            Order order = new Order("ORD-" + System.currentTimeMillis(), customerId, amount);
            orderCreatedCounter.increment();
            return order;
        } catch (Exception e) {
            orderFailedCounter.increment();
            throw e;
        }
    }
}