// 错误场景1：JSON字段名与Java属性不匹配
// 请求: {"user_name": "alice", "email": "alice@example.com"}
// Java: private String username;  // 默认匹配不上
class User1 {
    private String username;  // 期望 "username"，但JSON是 "user_name"
}

// 解决方案：使用@JsonProperty
class User2 {
    @JsonProperty("user_name")
    private String username;
}

// 错误场景2：日期格式不匹配
// 请求: {"birthday": "2024-01-15"}
// 默认期望ISO格式，非ISO格式会报错
class User3 {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}

// 错误场景3：缺少必需字段
// 请求: {"username": "alice"}（缺少email）
// 如果email没有默认值，会反序列化失败
class User4 {
    private String username;
    private String email;  // 没有默认值，反序列化可能失败
}