package com.example.demo.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

// 该配置类在DataSourceAutoConfiguration之后加载
// 当你需要使用DataSource时，确保它已经就绪
// 如果你需要在某个自动配置之后执行自己的配置，使用@AutoConfigureAfter
// 这个注解只影响自动配置类的加载顺序，不影响普通@Configuration类的加载
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MyCustomAutoConfiguration {
    // 可以安全地使用DataSource，因为此时它已经就绪
    // 但是需要注意，这里只能保证加载顺序，不能保证DataSource Bean已经创建
    // 如果需要保证Bean存在，使用@ConditionalOnBean注解
}