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