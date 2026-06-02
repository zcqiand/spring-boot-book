@RestController
@RequestMapping("/api/cart")
public class CartController {

    // 获取单个Cookie
    @GetMapping("/count")
    public String getCartCount(@CookieValue("cart_id") String cartId) {
        return "购物车ID: " + cartId;
    }

    // Cookie不存在时的默认值（Spring 5.3+）
    @GetMapping("/visitor")
    public String getVisitorId(
            @CookieValue(defaultValue = "anonymous") String visitorId) {
        return "访客ID: " + visitorId;
    }

    // 获取带尝试验证的Cookie（required=false + 包装类型）
    @GetMapping("/theme")
    public String getTheme(@CookieValue(required = false) String theme) {
        return theme != null ? "主题: " + theme : "使用默认主题";
    }
}