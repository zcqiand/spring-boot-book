@Service
@RequiredArgsConstructor
@Slf4j
public class TenantRegistrationService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private PlanService planService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private TenantInitializationService initializationService;

    @Transactional
    public Tenant register(TenantRegisterRequest request) {
        // 1. 验证企业信息
        validateBusinessInfo(request);

        // 2. 创建租户记录
        Tenant tenant = Tenant.builder()
            .companyName(request.getCompanyName())
            .businessLicense(request.getBusinessLicense())
            .contactName(request.getContactName())
            .contactPhone(request.getContactPhone())
            .contactEmail(request.getContactEmail())
            .status(TenantStatus.PENDING_APPROVAL)
            .build();

        tenant = tenantRepository.save(tenant);

        // 3. 创建订阅
        Subscription subscription = subscriptionService.createSubscription(
            tenant.getId(),
            request.getPlanId(),
            request.getCycle()
        );

        // 4. 初始化租户空间
        initializationService.initializeTenant(tenant, request.getPlanId());

        // 5. 发送欢迎邮件
        sendWelcomeEmail(tenant);

        log.info("租户注册成功: {}", tenant.getId());
        return tenant;
    }

    private void validateBusinessInfo(TenantRegisterRequest request) {
        // 检查统一社会信用代码是否已注册
        if (tenantRepository.existsByBusinessLicense(request.getBusinessLicense())) {
            throw new BusinessException("该企业已注册");
        }

        // 验证企业资质（可选：对接第三方API）
        if (!validateWithThirdParty(request.getBusinessLicense())) {
            throw new BusinessException("企业资质验证失败");
        }
    }
}