package com.lab.manager.controller;

import com.lab.manager.annotation.DataPermission;
import com.lab.manager.annotation.RequirePermission;
import com.lab.security.service.ExperimentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/experiments")
@RequiredArgsConstructor
public class ExperimentController {

    private final ExperimentService experimentService;

    /**
     * 查看实验记录 - 需要实验查看权限，且应用数据权限过滤
     */
    @GetMapping
    @PreAuthorize("hasAuthority('system:experiment:read')")
    @DataPermission(resourceType = "experiment")
    public ResponseEntity<?> listExperiments(
            @RequestParam(required = false) Long projectId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(experimentService.listExperiments(projectId, page, size));
    }

    /**
     * 查看单个实验详情 - 需要实验查看权限
     * 数据权限：只能查看属于自己负责项目的实验
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:experiment:read')")
    @DataPermission(resourceType = "experiment")
    public ResponseEntity<?> getExperiment(@PathVariable Long id) {
        return ResponseEntity.ok(experimentService.getExperiment(id));
    }

    /**
     * 创建实验记录 - 需要实验管理权限
     */
    @PostMapping
    @PreAuthorize("hasAuthority('system:experiment:write')")
    @RequirePermission(value = "system:experiment:write", dataScope = "own")
    public ResponseEntity<?> createExperiment(@RequestBody ExperimentDTO dto) {
        return ResponseEntity.ok(experimentService.createExperiment(dto));
    }

    /**
     * 删除实验记录 - 需要实验删除权限（高危操作）
     * 只有 ADMIN 或 MANAGER 可以执行
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:experiment:delete') and hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> deleteExperiment(@PathVariable Long id) {
        experimentService.deleteExperiment(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 导出实验数据 - 需要导出权限
     * 数据权限：只能导出自己权限范围内的数据
     */
    @GetMapping("/export")
    @PreAuthorize("hasAuthority('system:experiment:read') and hasAuthority('system:report:export')")
    @DataPermission(resourceType = "experiment")
    public ResponseEntity<?> exportExperiments(
            @RequestParam(required = false) Long projectId) {
        return ResponseEntity.ok(experimentService.exportExperiments(projectId));
    }

    /**
     * 批量操作实验记录 - 需要实验管理权限
     * 角色检查：只有 TECHNICIAN 及以上角色可以执行
     */
    @PostMapping("/batch")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TECHNICIAN')")
    @RequirePermission("system:experiment:write")
    public ResponseEntity<?> batchOperate(@RequestBody BatchOperationDTO dto) {
        return ResponseEntity.ok(experimentService.batchOperate(dto));
    }
}