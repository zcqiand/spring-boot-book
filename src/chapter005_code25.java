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