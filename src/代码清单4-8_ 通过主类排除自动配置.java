// Spring Boot 3.x语法：使用excludeName指定类的全限定名
// 排除这些配置后，你需要手动提供替代配置，否则应用可能无法启动
// 这种方式适合那些你确定不需要的自动配置，比如你知道自己会手动配置数据源
// 注意：排除后必须手动提供所需的替代配置，否则应用启动失败
@SpringBootApplication(excludeName = {
    "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
    "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
    "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"
})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}