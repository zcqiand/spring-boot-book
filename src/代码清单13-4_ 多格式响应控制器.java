package com.xrtech.api.controller;

import com.xrtech.api.domain.User;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ContentNegotiationController {

    // 根据Accept header自动选择JSON或XML
    @GetMapping(produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE
    })
    public List<User> listUsers() {
        return List.of(
            new User(1L, "alice@example.com"),
            new User(2L, "bob@example.com")
        );
    }

    // 强制指定XML格式
    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getUsersXml() {
        String xml = "<?xml version=\"1.0\"?><users>" +
            "<user><id>1</id><email>alice@example.com</email></user>" +
            "</users>";
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .body(xml);
    }
}

record User(Long id, String email) {}