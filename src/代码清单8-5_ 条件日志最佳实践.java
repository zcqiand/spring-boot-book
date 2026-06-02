public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public void createOrder(Order order) {
        // 最佳实践1：isDebugEnabled检查
        if (logger.isDebugEnabled()) {
            logger.debug("创建订单: {}", JSON.toJSONString(order));
        }

        // 最佳实践2：参数化日志（自动条件判断）
        logger.info("用户 {} 创建订单", order.getUserId());

        try {
            // 业务逻辑
        } catch (Exception e) {
            // 最佳实践3：使用ERROR级别，记录异常信息
            logger.error("创建订单失败，用户: {}, 错误: {}",
                order.getUserId(), e.getMessage(), e);
        }
    }
}