package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 方式一：使用组合注解（推荐）
// 这是最常用的写法，一个注解搞定三个功能
// 如果你不需要排除任何自动配置，不需要自定义扫描范围，就用这个最简单的写法
//组合注解的好处是简洁，但如果你需要精确控制每个行为，就需要使用方式二
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// 方式二：显式使用三个元注解（等价的另一种写法）
// 当你需要精确控制每个注解的行为时，可以使用这种方式
// 比如你只想启用自动配置，但不想启用组件扫描（这种情况很少见）
// 或者你想同时使用多个配置类，使用@Import导入它们
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// 方式三：排除特定自动配置类（Spring Boot 3.x语法）
// excludeName需要使用类的全限定名
// 当你需要完全手动控制某个功能时，可以通过exclude排除自动配置
// 下面的例子展示了如何排除数据源相关的三个自动配置类
// 这是Spring Boot 3.x的语法，使用全限定名字符串而不是Class对象
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