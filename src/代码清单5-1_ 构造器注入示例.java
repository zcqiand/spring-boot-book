@Service
public class OrderService {
    private final PaymentService paymentService; // final不可变
    private final OrderRepository orderRepository;

    // 构造器注入
    public OrderService(PaymentService paymentService, OrderRepository orderRepository) {
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
    }
}