@Component
public class OrderQueueMonitor {
    private final ConcurrentLinkedQueue<Order> orderQueue = new ConcurrentLinkedQueue<>();

    public OrderQueueMonitor(MeterRegistry meterRegistry) {
        Gauge.builder("business.order.queue.size", orderQueue, queue -> queue.size())
                .description("当前待处理订单队列长度")
                .register(meterRegistry);
    }
}