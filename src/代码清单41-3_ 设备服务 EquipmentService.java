package com.lab.equipment.service;

import com.lab.equipment.entity.Equipment;
import com.lab.equipment.entity.EquipmentCalibration;
import com.lab.equipment.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    public List<Equipment> getEquipmentsByLab(Long labId) {
        return equipmentRepository.findByLabId(labId);
    }

    public Equipment getEquipment(Long id) {
        return equipmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("设备不存在"));
    }

    @Transactional
    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Transactional
    public Equipment updateEquipment(Long id, Equipment updated) {
        Equipment equipment = getEquipment(id);
        equipment.setName(updated.getName());
        equipment.setModel(updated.getModel());
        equipment.setStatus(updated.getStatus());
        return equipmentRepository.save(equipment);
    }

    @Transactional
    public EquipmentCalibration recordCalibration(Long equipmentId, EquipmentCalibration calibration) {
        Equipment equipment = getEquipment(equipmentId);
        calibration.setEquipment(equipment);
        equipment.getCalibrations().add(calibration);
        equipmentRepository.save(equipment);
        return calibration;
    }

    public List<Equipment> getEquipmentsNeedingCalibration() {
        return equipmentRepository.findAll().stream()
            .filter(Equipment::needsCalibration)
            .toList();
    }
}