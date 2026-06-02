package com.lab.collection.service;

import com.lab.collection.dto.*;
import com.lab.equipment.entity.Equipment;
import com.lab.equipment.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataValidationService {

    private final EquipmentRepository equipmentRepository;

    public ValidationResult validate(MeasurementData data) {
        // 1. 检查设备是否存在
        Equipment equipment = equipmentRepository.findById(data.getEquipmentId())
            .orElse(null);
        if (equipment == null) {
            return ValidationResult.fail("设备不存在");
        }

        // 2. 检查设备是否在启用状态
        if (!"ACTIVE".equals(equipment.getStatus())) {
            return ValidationResult.fail("设备当前状态不允许采集");
        }

        // 3. 检查数值是否为空
        if (data.getValue() == null) {
            return ValidationResult.fail("测量值为空");
        }

        // 4. 检查数值是否在合理范围内
        if (data.getValue().isNaN() || data.getValue().isInfinite()) {
            return ValidationResult.fail("测量值无效");
        }

        // 5. 检查时间戳是否合理（不早于设备购置日期，不晚于当前时间）
        if (data.getTimestamp().isBefore(equipment.getPurchaseDate().atStartOfDay())) {
            return ValidationResult.fail("时间戳早于设备购置日期");
        }

        return ValidationResult.pass();
    }
}