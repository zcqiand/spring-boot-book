package com.xrtech.api.controller;

import com.xrtech.api.request.UserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/form")
public class FormController {

    /**
     * 表单提交 - 手动处理验证错误
     * 使用场景：返回错误消息给前端，而非抛出异常
     */
    @PostMapping("/submit")
    public String submit(
            @Validated UserRequest request,
            BindingResult bindingResult,
            Model model) {

        // 如果验证失败，不抛异常，返回错误信息
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            model.addAttribute("errors", errors);
            model.addAttribute("user", request);
            return "form";  // 返回表单页，显示错误
        }

        // 验证通过，处理业务
        return "redirect:/success";
    }

    /**
     * REST风格 - 返回JSON错误（无需BindingResult，全局异常处理）
     */
    @PostMapping("/api/submit")
    @ResponseBody
    public Map<String, Object> apiSubmit(
            @Validated @RequestBody UserRequest request) {
        // 如有验证错误会被全局异常处理器捕获
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", request.getUsername());
        return result;
    }
}