package com.xrtech.jpa.repository;

import com.xrtech.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户Repository接口
 *
 * 继承层级说明：
 * JpaRepository<User, Long>
 *     └─ PagingAndSortingRepository<User, Long>
 *             └─ CrudRepository<User, Long>
 *                     └─ Repository<T, ID>
 *
 * 各层提供的核心方法：
 * - Repository：空接口，仅作为标记
 * - CrudRepository：save, findById, findAll, delete, count, exists
 * - PagingAndSortingRepository：findAll(Pageable), findAll(Sort)
 * - JpaRepository：flush, saveAndFlush, deleteInBatch（额外JPA特性）
 */
@Repository  // Spring组件注解（可选，JpaRepository子接口已被@Repository标记）
public interface UserRepository extends JpaRepository<User, Long> {

    // ========== 方法命名查询（Spring Data根据方法名自动解析）==========

    /**
     * 根据用户名查找用户
     * 命名规则：findBy + 属性名（首字母大写）
     * 生成SQL：SELECT * FROM users WHERE username = ?
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱模糊查询用户（用于搜索功能）
     * 命名规则：findBy + 属性名 + Containing
     * 生成SQL：SELECT * FROM users WHERE email LIKE CONCAT('%', :email, '%')
     * 注意：Containing 不忽略大小写，如需忽略大小写应使用 ContainingIgnoreCase
     */
    List<User> findByEmailContains(String email);

    /**
     * 查找创建时间在指定时间之后的用户
     * 支持的关键词：After, Before, Between, LessThan, GreaterThan
     */
    List<User> findByCreatedAtAfter(java.time.LocalDateTime createdAt);

    // ========== 组合条件查询 ==========

    /**
     * 根据用户名和邮箱查询用户
     * 多个条件用And/Or连接
     */
    Optional<User> findByUsernameAndEmail(String username, String email);

    /**
     * 根据用户名或邮箱查询（任一匹配即可）
     */
    List<User> findByUsernameOrEmail(String username, String email);

    // ========== 分页和排序 ==========

    /**
     * 分页查询（Spring Data自动处理分页参数）
     * 注意：返回类型为Page，包含了分页信息
     */
    org.springframework.data.domain.Page<User> findByUsernameContaining(String username, org.springframework.data.domain.Pageable pageable);

    /**
     * 排序查询
     * Sort.by("字段名").ascending() / .descending()
     */
    List<User> findAllByOrderByCreatedAtDesc(org.springframework.data.domain.Sort sort);

    // ========== 自定义JPQL查询 ==========

    /**
     * 使用JPQL（Java Persistence Query Language）自定义查询
     * @Query注解：写在方法上，替代方法命名查询
     * @Param注解：绑定命名参数（代替?1, ?2位置参数）
     *
     * JPQL语法：使用实体名和属性名，而非表名和列名
     */
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsernameQuery(@Param("username") String username);

    /**
     * 模糊搜索 + 分页的JPQL示例
     */
    @Query("SELECT u FROM User u WHERE u.email LIKE %:keyword% ORDER BY u.createdAt DESC")
    List<User> searchByEmail(@Param("keyword") String keyword);
}