@RestController
@RequestMapping("/api/client")
public class ClientInfoController {

    // 获取单个请求头
    @GetMapping("/ua")
    public String getUserAgent(@RequestHeader("User-Agent") String userAgent) {
        return "浏览器: " + userAgent;
    }

    // 获取带默认值的请求头
    @GetMapping("/lang")
    public String getLanguage(
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String lang) {
        return "语言: " + lang;
    }

    // 获取所有请求头（Map形式）
    @GetMapping("/headers")
    public String getAllHeaders(@RequestHeader Map<String, String> headers) {
        return "Host: " + headers.get("host");
    }

    // 获取带验证的请求头
    @GetMapping("/auth")
    public String getAuthHeader(
            @RequestHeader("Authorization") String auth) {
        // 通常用于 Bearer Token
        return "Token: " + auth;
    }
}