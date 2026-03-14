package com.cfo.cfobot.service;

import com.cfo.cfobot.dto.AuthRequest;
import com.cfo.cfobot.dto.AuthResponse;
import com.cfo.cfobot.dto.RegisterRequest;

import jakarta.servlet.http.HttpSession;

public interface AuthService {

    AuthResponse register(RegisterRequest request, HttpSession session);

    AuthResponse login(AuthRequest request, HttpSession session);

    AuthResponse getCurrentUser(HttpSession session);

    AuthResponse logout(HttpSession session);
}
