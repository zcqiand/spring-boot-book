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