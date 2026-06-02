package com.example.monitoring.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 业务服务健康检查指示器
 * 检查点：订单服务、支付服务、用户服务可用性
 */
@Component("serviceHealth")
public class ServiceHealthIndicator implements HealthIndicator {

    private final AtomicBoolean orderServiceUp = new AtomicBoolean(true);
    private final AtomicBoolean paymentServiceUp = new AtomicBoolean(true);
    private final AtomicBoolean userServiceUp = new AtomicBoolean(true);
    private final AtomicInteger orderCount = new AtomicInteger(0);
    private final AtomicInteger failedOrderCount = new AtomicInteger(0);

    @Override
    public Health health() {
        boolean allHealthy = orderServiceUp.get()
                && paymentServiceUp.get()
                && userServiceUp.get();

        Health.Builder builder = allHealthy ? Health.up() : Health.down();

        builder.withDetail("orderService", orderServiceUp.get() ? "UP" : "DOWN")
               .withDetail("paymentService", paymentServiceUp.get() ? "UP" : "DOWN")
               .withDetail("userService", userServiceUp.get() ? "UP" : "DOWN")
               .withDetail("orderCount", orderCount.get())
               .withDetail("failedOrderCount", failedOrderCount.get())
               .withDetail("successRate", calculateSuccessRate());

        if (!allHealthy) {
            builder.withDetail("error", "One or more services are down");
        }

        return builder.build();
    }

    private String calculateSuccessRate() {
        int total = orderCount.get();
        if (total == 0) return "100%";
        int failed = failedOrderCount.get();
        return String.format("%.2f%%", (total - failed) * 100.0 / total);
    }

    public void setOrderServiceUp(boolean up) {
        this.orderServiceUp.set(up);
    }

    public void setPaymentServiceUp(boolean up) {
        this.paymentServiceUp.set(up);
    }

    public void setUserServiceUp(boolean up) {
        this.userServiceUp.set(up);
    }
}