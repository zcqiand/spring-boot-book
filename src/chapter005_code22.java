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