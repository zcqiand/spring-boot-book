@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Timed(value = "order.create", description = "订单创建耗时")
    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }
}