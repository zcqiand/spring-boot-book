@SpringBootApplication
@ComponentScan(
    basePackages = {"com.example.demo", "com.example.common"},
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com\\.example\\.demo\\.config\\..*"
    )
)
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
// 以上配置会扫描com.example.demo和com.example.common两个包
// 同时排除com.example.demo.config包下的所有类（使用正则表达式匹配）
// 这种配置适用于多模块项目，不同模块在不同包下
// 或者当你需要排除某些配置类，避免它们被自动扫描到时使用
// excludeFilters可以排除特定模式的类，这在排除测试配置或本地开发配置时很有用