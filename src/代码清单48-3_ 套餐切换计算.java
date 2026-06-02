@Service
@RequiredArgsConstructor
public class PlanSwitchCalculator {

    /**
     * 计算升级费用
     * 升级立即生效，剩余天数按新套餐日费折算
     */
    public Money calculateUpgradeCost(Tenant tenant, Plan targetPlan) {
        Subscription current = tenant.getActiveSubscription();
        Plan currentPlan = current.getPlan();

        // 计算剩余天数
        int remainingDays = current.getEndAt().until(LocalDate.now(), ChronoUnit.DAYS);

        // 新套餐日费
        Money newPlanDailyRate = targetPlan.getMonthlyPrice()
            .divide(30, RoundingMode.HALF_UP);

        // 当前套餐剩余价值
        Money currentRemaining = currentPlan.getMonthlyPrice()
            .divide(30, RoundingMode.HALF_UP)
            .multiply(remainingDays);

        // 需补差价
        Money upgradeCost = newPlanDailyRate.multiply(remainingDays)
            .subtract(currentRemaining);

        return upgradeCost.max(ZERO); // 不为负
    }

    /**
     * 计算降级费用
     * 降级在当前周期结束后生效
     */
    public Money calculateDowngradeCost(Tenant tenant, Plan targetPlan) {
        // 降级立即生效才收费，如果周期末生效则不收费
        // 简化处理：降级不收费，用新套餐价格续费即可
        return ZERO;
    }
}