package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiDemoApplication.class, args);

        // 获取Bean并调用方法
        UserService userService = context.getBean(UserService.class);
        String result = userService.registerUser("ZhangSan", "zhangsan@example.com");
        System.out.println(result);

        // 关闭容器
        context.close();
    }
}