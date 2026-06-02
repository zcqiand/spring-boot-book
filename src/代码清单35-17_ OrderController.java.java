@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Timed(value = "exercise.api.order.create", description = "创建订单API耗时")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(
            @RequestParam String customerId, @RequestParam BigDecimal amount) {
        Order order = orderService.createOrder(customerId, amount);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderNo", order.getOrderNo());
        return ResponseEntity.ok(response);
    }

    @Timed(value = "exercise.api.order.get", description = "查询订单API耗时")
    @GetMapping("/{orderNo}")
    public ResponseEntity<Map<String, Object>> getOrder(@PathVariable String orderNo) {
        Order order = orderService.getOrder(orderNo);
        Map<String, Object> response = new HashMap<>();
        response.put("success", order != null);
        response.put("order", order);
        return ResponseEntity.ok(response);
    }
}