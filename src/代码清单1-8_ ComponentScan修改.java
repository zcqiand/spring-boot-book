@SpringBootApplication
@ComponentScan(basePackages = {"com.example.controller", "com.example.demo"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}