package com.lab.collection.service;

import com.lab.collection.dto.*;
import com.lab.collection.entity.*;
import com.lab.collection.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertRuleRepository ruleRepository;

    public void checkAlerts(MeasurementData data) {
        var rules = ruleRepository.findByEquipmentId(data.getEquipmentId());

        for (AlertRule rule : rules) {
            if (rule.isTriggered(data.getValue())) {
                sendAlert(data, rule);
            }
        }
    }

    private void sendAlert(MeasurementData data, AlertRule rule) {
        log.warn("触发告警:设备={},规则={},值={}",
            data.getEquipmentId(), rule.getName(), data.getValue());

        Alert alert = Alert.builder()
            .equipmentId(data.getEquipmentId())
            .ruleId(rule.getId())
            .alertType(rule.getAlertType())
            .level(rule.getLevel())
            .message(rule.formatMessage(data.getValue()))
            .measurementValue(data.getValue())
            .timestamp(data.getTimestamp())
            .build();

        alertRepository.save(alert);
    }

    public void sendDataValidationAlert(MeasurementData data, String reason) {
        log.warn("数据校验告警:设备={},原因={}", data.getEquipmentId(), reason);
    }

    public void sendOutOfRangeAlert(MeasurementData data) {
        log.warn("数据超范围告警:设备={},值={}", data.getEquipmentId(), data.getValue());
    }
}