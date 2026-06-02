package com.xrtech.jpa.service;

import com.xrtech.jpa.entity.User;
import com.xrtech.jpa.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务层
 *
 * @Transactional：
 * - Spring会对所有public方法开启事务管理
 * - 默认配置：运行时异常回滚，检查异常提交
 * - 读取操作可指定readOnly=true提升性能
 */
@Service
@Transactional  // 开启事务管理
public class UserService {

    private final UserRepository userRepository;

    /**
     * 构造器注入（推荐方式）
     * Spring Data JPA的Repository会被自动注入
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ==================== 基础CRUD操作 ====================

    /**
     * 保存用户（insert或update）
     *
     * @param user 要保存的用户实体
     * @return 保存后的用户（含自增ID）
     *
     * save()行为说明：
     * - user.id == null：执行insert
     * - user.id != null：执行update
     * - 返回保存后的实体（含数据库生成的值）
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 根据ID查找用户
     *
     * @param id 主键ID
     * @return Optional包装的用户（可能为空）
     *
     * findById()返回Optional的原因：
     * - 提醒调用者处理"找不到"的情况
     * - 避免返回null导致空指针异常
     */
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 查询所有用户
     *
     * 注意：数据量大时不应直接使用findAll()
     * 应配合分页或限制返回数量
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 统计用户总数
     */
    public long countUsers() {
        return userRepository.count();
    }

    /**
     * 判断用户是否存在
     */
    public boolean existsUser(Long id) {
        return userRepository.existsById(id);
    }

    /**
     * 删除用户
     *
     * @param id 要删除的用户ID
     * @throws org.springframework.dao.EmptyResultDataAccessException 当用户不存在时
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ==================== 方法命名查询示例 ====================

    /**
     * 根据用户名查找用户
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 根据邮箱模糊搜索
     */
    public List<User> searchByEmail(String emailKeyword) {
        return userRepository.findByEmailContains(emailKeyword);
    }

    /**
     * 根据用户名和邮箱精确查询
     */
    public Optional<User> findByUsernameAndEmail(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email);
    }

    // ==================== 分页和排序 ====================

    /**
     * 分页查询（第N页，每页M条）
     *
     * @param pageNum 当前页码（从0开始）
     * @param pageSize 每页数量
     * @return 分页结果（包含总页数、总记录数等元数据）
     *
     * Page对象包含的信息：
     * - content：当前页的数据列表
     * - totalElements：总记录数
     * - totalPages：总页数
     * - number：当前页码
     * - size：每页大小
     * - first/last：是否为第一页/最后一页
     */
    public Page<User> findUsersByPage(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return userRepository.findAll(pageable);
    }

    /**
     * 分页 + 按指定字段排序
     *
     * @param pageNum 当前页码（从0开始）
     * @param pageSize 每页数量
     * @param sortField 排序字段名
     * @param sortDirection 排序方向（ASC或DESC）
     */
    public Page<User> findUsersByPageSorted(int pageNum, int pageSize, String sortField, Sort.Direction sortDirection) {
        Sort sort = Sort.by(sortDirection, sortField);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return userRepository.findAll(pageable);
    }

    /**
     * 模糊搜索 + 分页
     */
    public Page<User> searchUsersByEmail(String emailKeyword, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending());
        return userRepository.findByUsernameContaining(emailKeyword, pageable);
    }

    // ==================== 批量操作 ====================

    /**
     * 批量保存用户
     *
     * saveAll()行为说明：
     * - 内部循环调用save()
     * - 每条记录单独执行insert（可能效率不高）
     * - 对于大量数据，考虑使用saveInBatch或原生批量插入
     */
    public Iterable<User> saveAllUsers(List<User> users) {
        return userRepository.saveAll(users);
    }

    /**
     * 删除所有用户（慎用）
     */
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    // ==================== 业务方法 ====================

    /**
     * 注册新用户
     * 业务逻辑：检查用户名和邮箱是否已存在
     */
    public User registerUser(String username, String email) {
        // 检查用户名是否存在
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("用户名已存在：" + username);
        }
        // 检查邮箱是否存在
        if (email != null && userRepository.findByEmailContains(email).size() > 0) {
            throw new IllegalArgumentException("邮箱已被注册：" + email);
        }
        // 创建并保存用户
        User user = User.builder()
                .username(username)
                .email(email)
                .build();
        return userRepository.save(user);
    }
}