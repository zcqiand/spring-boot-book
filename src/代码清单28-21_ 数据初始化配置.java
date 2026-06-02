@Configuration
public class DataInitConfig {

    @Bean
    CommandLineRunner initData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = new Role("ADMIN");
            Role userRole = new Role("USER");
            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            User admin = new User("admin", passwordEncoder.encode("admin123"));
            admin.addRole(adminRole);
            admin.addRole(userRole);
            userRepository.save(admin);

            User user = new User("user", passwordEncoder.encode("user123"));
            user.addRole(userRole);
            userRepository.save(user);

            System.out.println("========== 数据初始化完成 ==========");
            System.out.println("管理员: admin / admin123 (ROLE_ADMIN, ROLE_USER)");
            System.out.println("普通用户: user / user123 (ROLE_USER)");
            System.out.println("====================================");
        };
    }
}