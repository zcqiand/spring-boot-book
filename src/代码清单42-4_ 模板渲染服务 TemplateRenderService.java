package com.lab.report.service;

import com.lab.report.entity.ReportTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TemplateRenderService {

    public String render(ReportTemplate template, Map<String, Object> data) {
        String content = template.getContent();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            Object value = entry.getValue();

            if (value instanceof String) {
                content = content.replace(placeholder, (String) value);
            } else if (value instanceof Map) {
                Map<String, Object> mapValue = (Map<String, Object>) value;
                for (Map.Entry<String, Object> subEntry : mapValue.entrySet()) {
                    String subPlaceholder = "{{" + entry.getKey() + "." + subEntry.getKey() + "}}";
                    content = content.replace(subPlaceholder, String.valueOf(subEntry.getValue()));
                }
            }
        }

        return content;
    }
}