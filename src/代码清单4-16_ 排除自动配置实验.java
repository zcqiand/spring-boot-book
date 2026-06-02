@SpringBootApplication(excludeName = {
    "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
public class ExclusionDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExclusionDemoApplication.class, args);
    }
}