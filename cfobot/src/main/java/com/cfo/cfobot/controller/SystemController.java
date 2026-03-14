package com.cfo.cfobot.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SystemController {

    @Operation(summary = "System information endpoint")
    @GetMapping("/api/system/info")
    public Map<String, Object> systemInfo() {

        Map<String, Object> info = new HashMap<>();

        info.put("application", "CFO Bot");
        info.put("version", "1.0");
        info.put("description", "Cloud Economics Cost Estimator");
        info.put("author", "Information Systems Project");
        info.put("architecture", "Spring Boot Monolithic Application");

        return info;
    }
}