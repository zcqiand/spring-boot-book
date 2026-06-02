package com.xrtech.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
            // 禁用URL后缀协商（.json/.xml）
            .favorParameter(false)
            .ignoreAcceptHeader(false)
            // 默认JSON格式
            .defaultContentType(MediaType.APPLICATION_JSON)
            // 配置媒体类型映射
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML);
    }
}