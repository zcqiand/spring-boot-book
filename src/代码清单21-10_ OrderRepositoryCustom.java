public interface OrderRepositoryCustom {
    List<Order> searchOrders(
        String orderNo, Long customerId, List<String> statuses,
        BigDecimal minAmount, BigDecimal maxAmount,
        LocalDateTime startTime, LocalDateTime endTime,
        String phonePrefix);

    OrderStatistics calculateStatistics(
        List<String> statuses, LocalDateTime startTime, LocalDateTime endTime);

    record OrderStatistics(
        long orderCount, BigDecimal totalAmount,
        BigDecimal averageAmount, BigDecimal maxAmount, BigDecimal minAmount) {}
}