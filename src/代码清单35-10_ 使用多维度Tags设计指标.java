public void processPayment(Order order, String paymentMethod) {
    Counter.builder("business.payment.total")
            .tag("method", paymentMethod)    // 支付方式
            .tag("channel", order.getChannel()) // 渠道
            .register(meterRegistry)
            .increment();
}