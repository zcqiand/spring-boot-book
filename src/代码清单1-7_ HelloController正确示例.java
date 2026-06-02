package com.example.demo.controller;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() { return "Hello, Spring Boot!"; }
}