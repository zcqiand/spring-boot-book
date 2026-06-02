package com.lab.collection.gateway;

import com.lab.collection.dto.*;
import com.lab.collection.service.DataProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataCollectionGateway {

    private final DataProcessService dataProcessService;

    /**
     * 处理来自仪器传感器的数据采集请求
     * 通常由仪器直接HTTP POST推送
     */
    public void handleInstrumentData(InstrumentDataRequest request) {
        log.info("收到仪器数据: 设备={}, 测量值={}",
            request.getEquipmentId(), request.getValue());

        RawMeasurementData rawData = RawMeasurementData.builder()
            .equipmentId(request.getEquipmentId())
            .timestamp(request.getTimestamp())
            .value(request.getValue())
            .unit(request.getUnit())
            .sourceType(SourceType.INSTRUMENT)
            .rawData(request.getRawData())
            .build();

        dataProcessService.process(rawData);
    }

    /**
     * 处理来自人工录入的数据
     * 通常由管理员界面提交
     */
    public void handleManualData(ManualDataRequest request) {
        log.info("收到人工录入数据: 设备={}, 测量值={}",
            request.getEquipmentId(), request.getValue());

        RawMeasurementData rawData = RawMeasurementData.builder()
            .equipmentId(request.getEquipmentId())
            .timestamp(request.getTimestamp())
            .value(request.getValue())
            .unit(request.getUnit())
            .sourceType(SourceType.MANUAL)
            .operatorId(request.getOperatorId())
            .build();

        dataProcessService.process(rawData);
    }

    /**
     * 处理来自第三方系统的API推送
     * 通常是定时推送或事件触发推送
     */
    public void handleThirdPartyData(ThirdPartyDataRequest request) {
        log.info("收到第三方数据: 系统={}, 设备={}",
            request.getSourceSystem(), request.getEquipmentId());

        RawMeasurementData rawData = RawMeasurementData.builder()
            .equipmentId(request.getEquipmentId())
            .timestamp(request.getTimestamp())
            .value(request.getValue())
            .unit(request.getUnit())
            .sourceType(SourceType.THIRD_PARTY)
            .sourceSystem(request.getSourceSystem())
            .build();

        dataProcessService.process(rawData);
    }
}