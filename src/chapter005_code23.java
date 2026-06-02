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