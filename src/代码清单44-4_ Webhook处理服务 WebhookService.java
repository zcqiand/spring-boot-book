package com.lab.integration.service;

import com.lab.integration.entity.WebhookConfig;
import com.lab.integration.entity.WebhookLog;
import com.lab.integration.repository.WebhookConfigRepository;
import com.lab.integration.repository.WebhookLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final WebhookConfigRepository configRepository;
    private final WebhookLogRepository logRepository;
    private final RestTemplate restTemplate;

    @Async
    public void sendWebhook(String eventType, Object payload) {
        List<WebhookConfig> configs = configRepository.findByEventType(eventType);

        for (WebhookConfig config : configs) {
            sendWebhookAsync(config, payload);
        }
    }

    private void sendWebhookAsync(WebhookConfig config, Object payload) {
        WebhookLog webhookLog = WebhookLog.builder()
            .configId(config.getId())
            .eventType(config.getEventType())
            .payload(payload.toString())
            .status("PENDING")
            .retryCount(0)
            .build();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Webhook-Signature", generateSignature(config.getSecret(), payload.toString()));

            HttpEntity<Object> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                config.getWebhookUrl(),
                request,
                String.class
            );

            webhookLog.setStatus(response.getStatusCode().is2xxSuccessful() ? "SUCCESS" : "FAILED");
            webhookLog.setResponseBody(response.getBody());

            log.info("Webhook发送成功: {}, 状态码: {}",
                config.getWebhookUrl(), response.getStatusCode());

        } catch (Exception e) {
            webhookLog.setStatus("FAILED");
            webhookLog.setErrorMessage(e.getMessage());
            log.warn("Webhook发送失败: {}, 错误: {}",
                config.getWebhookUrl(), e.getMessage());

            handleFailure(config, webhookLog, e);
        }

        logRepository.save(webhookLog);
    }

    private void handleFailure(WebhookConfig config, WebhookLog webhookLog, Exception e) {
        if (webhookLog.getRetryCount() < config.getMaxRetries()) {
            webhookLog.setStatus("RETRYING");
            webhookLog.setNextRetryAt(LocalDateTime.now().plusMinutes(config.getRetryInterval()));
            logRepository.save(webhookLog);

            log.info("Webhook将重试: {}次后", config.getMaxRetries() - webhookLog.getRetryCount());
        } else {
            webhookLog.setStatus("EXHAUSTED");
            logRepository.save(webhookLog);

            log.error("Webhook重试次数已用完: {}", config.getWebhookUrl());
        }
    }

    private String generateSignature(String secret, String payload) {
        return secret;
    }
}