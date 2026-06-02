// 传统方式：主动创建依赖
public class OrderService {
    private PaymentService paymentService = new PaymentService(); // 我自己new
}