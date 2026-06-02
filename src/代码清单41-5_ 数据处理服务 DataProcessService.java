package com.lab.collection.service;

import com.lab.collection.dto.*;
import com.lab.collection.entity.*;
import com.lab.collection.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataProcessService {

    private final MeasurementDataRepository dataRepository;
    private final DataValidationService validationService;
    private final AlertService alertService;
    private final EquipmentRepository equipmentRepository;

    /**
     * 数据处理主管道
     * 接收原始数据 -> 解析 -> 校验 -> 存储 -> 告警
     */
    @Transactional
    public void process(RawMeasurementData rawData) {
        // Step 1: 数据解析
        MeasurementData data = parse(rawData);

        // Step 2: 数据校验
        ValidationResult validation = validationService.validate(data);
        if (!validation.isValid()) {
            log.warn("数据校验失败:设备={},原因={}", data.getEquipmentId(), validation.getMessage());
            alertService.sendDataValidationAlert(data, validation.getMessage());
            return;
        }

        // Step 3: 范围校验
        if (!isValueInRange(data)) {
            log.warn("数据超出范围:设备={},值={}", data.getEquipmentId(), data.getValue());
            alertService.sendOutOfRangeAlert(data);
        }

        // Step 4: 存储
        MeasurementData saved = dataRepository.save(data);
        log.info("数据存储成功:设备={},时间={},值={}",
            saved.getEquipmentId(), saved.getTimestamp(), saved.getValue());

        // Step 5: 实时告警检查
        alertService.checkAlerts(saved);
    }

    private MeasurementData parse(RawMeasurementData raw) {
        return MeasurementData.builder()
            .equipmentId(raw.getEquipmentId())
            .timestamp(raw.getTimestamp())
            .value(raw.getValue())
            .unit(raw.getUnit())
            .sourceType(raw.getSourceType().name())
            .quality(raw.getQuality())
            .build();
    }

    private boolean isValueInRange(MeasurementData data) {
        Equipment equipment = equipmentRepository.findById(data.getEquipmentId())
            .orElse(null);
        if (equipment == null) return true;

        Double minValue = equipment.getMinValue();
        Double maxValue = equipment.getMaxValue();
        if (minValue == null || maxValue == null) return true;

        return data.getValue() >= minValue && data.getValue() <= maxValue;
    }
}