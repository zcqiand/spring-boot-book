package com.lab.tenant;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class TenantContext {

    private String tenantId;

    public String getCurrentTenant() {
        return tenantId;
    }

    public void setCurrentTenant(String tenantId) {
        this.tenantId = tenantId;
    }
}