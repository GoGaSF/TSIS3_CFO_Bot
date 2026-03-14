package com.cfo.cfobot.controller;

import com.cfo.cfobot.dto.AuthRequest;
import com.cfo.cfobot.dto.AuthResponse;
import com.cfo.cfobot.dto.RegisterRequest;
import com.cfo.cfobot.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request, HttpSession session) {
        return authService.register(request, session);
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request, HttpSession session) {
        return authService.login(request, session);
    }

    @Operation(summary = "Get current user")
    @GetMapping("/me")
    public AuthResponse me(HttpSession session) {
        return authService.getCurrentUser(session);
    }

    @Operation(summary = "Logout user")
    @PostMapping("/logout")
    public AuthResponse logout(HttpSession session) {
        return authService.logout(session);
    }
}