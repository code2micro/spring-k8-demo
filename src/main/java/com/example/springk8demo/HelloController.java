package com.example.springk8demo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final String applicationName;
    private final String configuredMessage;

    public HelloController(
            @Value("${spring.application.name}") String applicationName,
            @Value("${app.message}") String configuredMessage) {
        this.applicationName = applicationName;
        this.configuredMessage = configuredMessage;
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello from Spring Boot");
    }

    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of(
                "application", applicationName,
                "version", "1.0");
    }

    @GetMapping("/configured-message")
    public Map<String, String> configuredMessage() {
        return Map.of("message", configuredMessage);
    }

    @GetMapping("/password")
    public Map<String, String> password() {
    return Map.of(
        "password",
        System.getenv("DB_PASSWORD")
    );
   
    }
    @GetMapping("/test123")
    public String test123() {
        return "TEST123";
    }
}
