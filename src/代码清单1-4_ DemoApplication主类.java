@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(DemoApplication.class, args);
            System.out.println("========================================");
            System.out.println("  Spring Boot 应用启动成功！");
            System.out.println("  访问地址：http://localhost:8080");
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("应用启动失败，请检查：");
            System.err.println("1. 端口 8080 是否被占用？");
            System.err.println("2. JDK 版本是否为 17+？");
            e.printStackTrace();
            System.exit(1);
        }
    }
}