package com.xrtech.chapter11.service;

import com.xrtech.chapter11.dto.*;

/**
 * 用户服务接口
 */
public interface UserService {
    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse create(UserCreateRequest request);
    UserResponse update(Long id, UserUpdateRequest request);
    UserResponse patch(Long id, UserPatchRequest request);
    void delete(Long id);
}