@RestController
@RequestMapping("/api/search")
public class SearchController {

    // 陷阱：默认required=true，客户端不传page参数会报400错误
    @GetMapping
    public String search(@RequestParam int page) {
        return "第" + page + "页";
    }

    // 正确做法1：明确标记可选，指定默认值
    @GetMapping("/safe")
    public String searchSafe(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return "第" + page + "页，每页" + size + "条";
    }

    // 正确做法2：使用包装类型（Integer）允许null，配合required=false
    @GetMapping("/optional")
    public String searchOptional(
            @RequestParam(required = false) Integer page) {
        if (page == null) {
            return "未指定页码，使用第1页";
        }
        return "第" + page + "页";
    }
}