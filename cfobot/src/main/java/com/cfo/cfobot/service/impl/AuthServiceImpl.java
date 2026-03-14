package com.cfo.cfobot.service.impl;

import com.cfo.cfobot.dto.AuthRequest;
import com.cfo.cfobot.dto.AuthResponse;
import com.cfo.cfobot.dto.RegisterRequest;
import com.cfo.cfobot.model.UserAccount;
import com.cfo.cfobot.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService {

    private final Map<String, UserAccount> users = new ConcurrentHashMap<>();

    public AuthServiceImpl() {
        users.put("demo@cfobot.com", new UserAccount("Demo User", "demo@cfobot.com", "123456"));
    }

    @Override
    public AuthResponse register(RegisterRequest request, HttpSession session) {
        validateRegisterRequest(request);

        String email = request.getEmail().trim().toLowerCase();

        if (users.containsKey(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists");
        }

        UserAccount user = new UserAccount(
                request.getName().trim(),
                email,
                request.getPassword()
        );

        users.put(email, user);

        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userName", user.getName());

        return new AuthResponse(true, "Registration successful", user.getName(), user.getEmail());
    }

    @Override
    public AuthResponse login(AuthRequest request, HttpSession session) {
        validateLoginRequest(request);

        String email = request.getEmail().trim().toLowerCase();
        UserAccount user = users.get(email);

        if (user == null || !user.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password");
        }

        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userName", user.getName());

        return new AuthResponse(true, "Login successful", user.getName(), user.getEmail());
    }

    @Override
    public AuthResponse getCurrentUser(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        String name = (String) session.getAttribute("userName");

        if (email == null || name == null) {
            return new AuthResponse(false, "Not authenticated", null, null);
        }

        return new AuthResponse(true, "User is authenticated", name, email);
    }

    @Override
    public AuthResponse logout(HttpSession session) {
        session.invalidate();
        return new AuthResponse(true, "Logout successful", null, null);
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is empty");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }

        if (request.getPassword().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must contain at least 6 characters");
        }
    }

    private void validateLoginRequest(AuthRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is empty");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }
    }
}