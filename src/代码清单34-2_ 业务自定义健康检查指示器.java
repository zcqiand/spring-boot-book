package com.example.monitoring.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicBoolean;

@Component("businessHealth")
public class BusinessHealthIndicator implements HealthIndicator {

    private final AtomicBoolean healthy = new AtomicBoolean(true);
    private volatile String lastError = "No errors";

    @Override
    public Health health() {
        // 检查核心业务状态
        boolean businessHealthy = checkOrderService()
                && checkPaymentService()
                && checkInventoryService();

        if (businessHealthy) {
            return Health.up()
                    .withDetail("orderService", "OK")
                    .withDetail("paymentService", "OK")
                    .withDetail("inventoryService", "OK")
                    .build();
        } else {
            return Health.down()
                    .withDetail("error", lastError)
                    .build();
        }
    }

    private boolean checkOrderService() {
        try {
            // 模拟订单服务健康检查
            return healthy.get();
        } catch (Exception e) {
            lastError = "OrderService failed: " + e.getMessage();
            return false;
        }
    }

    private boolean checkPaymentService() {
        try {
            // 模拟支付服务健康检查
            return healthy.get();
        } catch (Exception e) {
            lastError = "PaymentService failed: " + e.getMessage();
            return false;
        }
    }

    private boolean checkInventoryService() {
        try {
            // 模拟库存服务健康检查
            return healthy.get();
        } catch (Exception e) {
            lastError = "InventoryService failed: " + e.getMessage();
            return false;
        }
    }

    public void setHealthy(boolean healthy) {
        this.healthy.set(healthy);
    }
}