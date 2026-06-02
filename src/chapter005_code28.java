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