// IoC方式：依赖由外部注入
public class OrderService {
    private PaymentService paymentService; // 容器帮我注入

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService; // 通过构造器接收
    }
}