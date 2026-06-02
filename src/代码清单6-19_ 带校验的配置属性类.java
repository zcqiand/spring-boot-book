package com.example.demo.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 应用配置属性类
 * 使用@Validated开启配置校验
 */
@Component
@ConfigurationProperties(prefix = "app.config")
@Validated
public class AppConfigProperties {

    // 必须提供，不能为空
    @NotBlank(message = "应用名称不能为空")
    private String appName;

    // 端口号范围校验
    @NotNull(message = "服务器端口不能为空")
    @Min(value = 1024, message = "端口号必须大于1024")
    @Max(value = 65535, message = "端口号必须小于65535")
    private Integer serverPort;

    // 最大连接数范围校验
    @Min(value = 1, message = "最大连接数至少为1")
    @Max(value = 200, message = "最大连接数不能超过200")
    private int maxConnections = 100;

    // 启用标志
    private boolean enabled = true;

    // 省略getter和setter
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }

    public Integer getServerPort() { return serverPort; }
    public void setServerPort(Integer serverPort) { this.serverPort = serverPort; }

    public int getMaxConnections() { return maxConnections; }
    public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}