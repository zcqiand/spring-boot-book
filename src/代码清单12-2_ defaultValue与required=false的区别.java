// 场景1：分页参数，客户端不传应有默认值
// 推荐：明确默认值，代码意图清晰
@GetMapping("/list")
public String list(
        @RequestParam(defaultValue = "1") int page) {
    // page 永远有值，不会是null
}

// 场景2：可选的筛选条件，可能完全不存在
// 推荐：required=false + 包装类型
@GetMapping("/search")
public String search(
        @RequestParam(required = false) String category) {
    if (category == null) {
        // 未指定分类，返回全部
    }
}

// 错误做法：required=false + 基本类型
// int无法为null，会报NullPointerException
@GetMapping("/wrong")
public String wrong(@RequestParam(required = false) int page) {
    return "第" + page + "页"; // page为null时会NPE
}