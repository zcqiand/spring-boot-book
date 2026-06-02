package com.xrtech.chapter12.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 * 使用multipart/form-data格式
 */
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private static final String UPLOAD_DIR = "/tmp/uploads/";

    /**
     * 单文件上传
     * POST /api/upload/image
     * Content-Type: multipart/form-data
     * Form field: file (二进制文件数据)
     */
    @PostMapping("/image")
    public Map<String, String> uploadImage(
            @RequestParam("file") MultipartFile file) throws IOException {

        Map<String, String> result = new HashMap<>();

        if (file.isEmpty()) {
            result.put("error", "文件不能为空");
            return result;
        }

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            result.put("error", "只支持图片文件");
            return result;
        }

        // 保存文件
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetPath = Paths.get(UPLOAD_DIR, filename);
        Files.createDirectories(targetPath.getParent());
        file.transferTo(targetPath);

        result.put("filename", filename);
        result.put("size", String.valueOf(file.getSize()));
        result.put("contentType", contentType);
        return result;
    }

    /**
     * 多文件上传
     * POST /api/upload/images
     * Form field: files (多个文件)
     */
    @PostMapping("/images")
    public Map<String, Object> uploadMultipleImages(
            @RequestParam("files") MultipartFile[] files) throws IOException {

        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path targetPath = Paths.get(UPLOAD_DIR, filename);
                    Files.createDirectories(targetPath.getParent());
                    file.transferTo(targetPath);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                }
            } else {
                failCount++;
            }
        }

        result.put("success", successCount);
        result.put("failed", failCount);
        result.put("total", files.length);
        return result;
    }

    /**
     * 混合参数上传（文件 + 文本字段）
     * POST /api/upload/with-meta
     * Form fields: file, name, description
     */
    @PostMapping("/with-meta")
    public Map<String, String> uploadWithMetadata(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description) throws IOException {

        Map<String, String> result = new HashMap<>();

        if (!file.isEmpty()) {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path targetPath = Paths.get(UPLOAD_DIR, filename);
            Files.createDirectories(targetPath.getParent());
            file.transferTo(targetPath);

            result.put("filename", filename);
            result.put("name", name);
            result.put("description", description != null ? description : "");
        }

        return result;
    }
}