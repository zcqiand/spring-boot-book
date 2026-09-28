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

// ───────────── 文件：src/main/java/com/example/demo/service/UserService.java ─────────────
package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 用户服务——演示构造器注入完整写法
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final MessageService messageService;

    /**
     * 使用构造器注入所有依赖
     * @Qualifier用于解决多个同类型Bean的歧义
     */
    @Autowired
    public UserService(
            @Qualifier("jdbcUserRepository") UserRepository userRepository,
            EmailService emailService,
            @Qualifier("smsMessageService") MessageService messageService) {

        this.userRepository = userRepository;
        this.emailService = emailService;
        this.messageService = messageService;
    }

    public String registerUser(String username, String email) {
        userRepository.save(username);
        emailService.sendWelcomeEmail(email);
        messageService.sendWelcomeMessage(username);
        return "用户注册成功：" + username;
    }
}

// ───────────── 文件：src/main/java/com/example/demo/service/UserRepository.java ─────────────
package com.example.demo.service;

/**
 * 用户仓储接口
 */
public interface UserRepository {
    void save(String username);
}

// ───────────── 文件：src/main/java/com/example/demo/service/JdbcUserRepository.java ─────────────
package com.example.demo.service;

import org.springframework.stereotype.Repository;

/**
 * JDBC实现——使用@Primary标记默认实现
 */
@Repository
@org.springframework.context.annotation.Primary
public class JdbcUserRepository implements UserRepository {

    @Override
    public void save(String username) {
        System.out.println("[JDBC] 保存用户：" + username);
    }
}

// ───────────── 文件：src/main/java/com/example/demo/service/JpaUserRepository.java ─────────────
package com.example.demo.service;

import org.springframework.stereotype.Repository;

/**
 * JPA实现
 */
@Repository
public class JpaUserRepository implements UserRepository {

    @Override
    public void save(String username) {
        System.out.println("[JPA] 保存用户：" + username);
    }
}

// ───────────── 文件：src/main/java/com/example/demo/service/EmailService.java ─────────────
package com.example.demo.service;

/**
 * 邮件服务接口
 */
public interface EmailService {
    void sendWelcomeEmail(String email);
}

// ───────────── 文件：src/main/java/com/example/demo/service/SmtpEmailService.java ─────────────
package com.example.demo.service;

import org.springframework.stereotype.Service;

/**
 * 邮件服务实现
 */
@Service
public class SmtpEmailService implements EmailService {

    @Override
    public void sendWelcomeEmail(String email) {
        System.out.println("[SMTP] 发送欢迎邮件至：" + email);
    }
}

// ───────────── 文件：src/main/java/com/example/demo/service/MessageService.java ─────────────
package com.example.demo.service;

/**
 * 消息服务接口
 */
public interface MessageService {
    void sendWelcomeMessage(String username);
}

// ───────────── 文件：src/main/java/com/example/demo/service/SmsMessageService.java ─────────────
package com.example.demo.service;

import org.springframework.stereotype.Service;

/**
 * 短信消息服务实现
 */
@Service("smsMessageService")
public class SmsMessageService implements MessageService {

    @Override
    public void sendWelcomeMessage(String username) {
        System.out.println("[SMS] 发送欢迎消息至：" + username);
    }
}

// ───────────── 文件：src/main/java/com/example/demo/service/WechatMessageService.java ─────────────
package com.example.demo.service;

import org.springframework.stereotype.Service;

/**
 * 微信消息服务实现
 */
@Service("wechatMessageService")
public class WechatMessageService implements MessageService {

    @Override
    public void sendWelcomeMessage(String username) {
        System.out.println("[WECHAT] 发送欢迎消息至：" + username);
    }
}