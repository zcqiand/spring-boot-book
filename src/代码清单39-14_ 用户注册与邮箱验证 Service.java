package com.lab.security.service;

import com.lab.security.entity.SysUser;
import com.lab.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    /**
     * 用户注册
     * 1. 验证邮箱唯一性
     * 2. 加密密码
     * 3. 创建用户（初始状态为未激活）
     * 4. 发送激活邮件
     */
    @Transactional
    public void register(UserRegistrationRequest request) {
        // 检查邮箱唯一性
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("邮箱已被注册");
        }

        // 检查用户名唯一性
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("用户名已被使用");
        }

        // 创建用户
        SysUser user = SysUser.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .email(request.getEmail())
            .mobile(request.getMobile())
            .enabled(false)  // 邮箱验证前禁用
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .accountNonExpired(true)
            .activationToken(UUID.randomUUID().toString())
            .activationTokenExpiresAt(LocalDateTime.now().plusHours(24))
            .build();

        userRepository.save(user);

        // 发送激活邮件
        sendActivationEmail(user);
    }

    /**
     * 激活用户账号
     */
    @Transactional
    public void activateUser(String activationToken) {
        SysUser user = userRepository.findByActivationToken(activationToken)
            .orElseThrow(() -> new IllegalArgumentException("无效的激活链接"));

        if (user.getActivationTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("激活链接已过期");
        }

        user.setEnabled(true);
        user.setActivationToken(null);
        user.setActivationTokenExpiresAt(null);
        userRepository.save(user);
    }

    /**
     * 发送激活邮件
     */
    private void sendActivationEmail(SysUser user) {
        String activationUrl = String.format(
            "https://your-domain.com/api/auth/activate?token=%s", user.getActivationToken());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("【实验室管理系统】账号激活");
        message.setText(String.format("""
            您好，%s：

            请点击以下链接激活您的账号（24小时内有效）：

            %s

            如果您没有注册过我们的系统，请忽略此邮件。

            此致
            实验室管理系统
            """, user.getUsername(), activationUrl));

        mailSender.send(message);
    }
}