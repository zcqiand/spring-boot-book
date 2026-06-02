@Service
@RequiredArgsConstructor
@Slf4j
public class BillingEngine {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private PaymentService paymentService;

    /**
     * 生成账单
     */
    @Transactional
    public Invoice generateInvoice(Subscription subscription) {
        Plan plan = subscription.getPlan();
        SubscriptionCycle cycle = subscription.getCycle();

        Money amount = calculateAmount(plan, cycle);
        Money tax = amount.multiply(0.06); // 增值税6%
        Money total = amount.add(tax);

        Invoice invoice = Invoice.builder()
            .subscriptionId(subscription.getId())
            .tenantId(subscription.getTenantId())
            .amount(amount)
            .tax(tax)
            .total(total)
            .status(InvoiceStatus.PENDING)
            .dueDate(LocalDate.now().plusDays(7))
            .build();

        invoice = invoiceService.save(invoice);

        // 发送账单通知
        sendInvoiceNotification(subscription.getTenant(), invoice);

        return invoice;
    }

    /**
     * 计算账单金额
     */
    private Money calculateAmount(Plan plan, SubscriptionCycle cycle) {
        Money basePrice;

        switch (cycle) {
            case MONTHLY:
                basePrice = plan.getMonthlyPrice();
                break;
            case QUARTERLY:
                basePrice = plan.getMonthlyPrice().multiply(3)
                    .multiply(0.95); // 季付95折
                break;
            case YEARLY:
                basePrice = plan.getMonthlyPrice().multiply(12)
                    .multiply(0.85); // 年付85折
                break;
            default:
                throw new IllegalArgumentException("不支持的订阅周期");
        }

        return basePrice;
    }

    /**
     * 处理支付
     */
    @Transactional
    public void processPayment(Invoice invoice, PaymentMethod method) {
        Payment payment = paymentService.processPayment(invoice, method);

        if (payment.isSuccess()) {
            invoice.setStatus(InvoiceStatus.PAID);
            invoice.setPaidAt(LocalDateTime.now());
            activateSubscription(invoice.getSubscription());
        } else {
            invoice.setStatus(InvoiceStatus.PAYMENT_FAILED);
            handlePaymentFailure(invoice, payment);
        }
    }

    private void activateSubscription(Subscription subscription) {
        subscription.activate();
        subscriptionRepository.save(subscription);
    }
}