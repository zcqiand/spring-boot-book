@Service
@RequiredArgsConstructor
public class AccountBindingService {

    @Autowired
    private PlatformUserRepository platformUserRepository;

    @Autowired
    private TenantUserMappingRepository tenantUserMappingRepository;

    /**
     * 绑定外部账号（支持第三方SSO账号关联）
     */
    @Transactional
    public void bindExternalAccount(Long userId, String provider, String externalId) {
        PlatformUser user = platformUserRepository.findById(userId)
            .orElseThrow(() -> new UserException("用户不存在"));

        // 检查是否已被其他账号绑定
        PlatformUser existing = platformUserRepository
            .findByExternalIdPId(provider, externalId);

        if (existing != null && !existing.getId().equals(userId)) {
            throw new UserException("该外部账号已被其他用户绑定");
        }

        // 更新绑定信息
        user.setSource(UserSource.valueOf(provider.toUpperCase()));
        user.setExternalIdPId(externalId);
        platformUserRepository.save(user);

        log.info("账号绑定成功: userId={}, provider={}", userId, provider);
    }

    /**
     * 发送绑定验证码
     */
    public void sendBindingVerification(String email, String tenantId) {
        // 生成6位验证码
        String code = String.format("%06d", new Random().nextInt(999999));

        // 存储验证码（带过期时间）
        redisTemplate.opsForValue().set(
            "bind:verify:" + email + ":" + tenantId,
            code,
            10,
            TimeUnit.MINUTES
        );

        // 发送邮件
        emailService.sendVerificationEmail(email, code);

        log.info("绑定验证码已发送: email={}, tenantId={}", email, tenantId);
    }
}