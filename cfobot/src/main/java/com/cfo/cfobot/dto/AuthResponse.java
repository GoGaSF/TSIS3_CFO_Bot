package com.cfo.cfobot.dto;

public class AuthResponse {

    private boolean success;
    private String message;
    private String name;
    private String email;

    public AuthResponse() {
    }

    public AuthResponse(boolean success, String message, String name, String email) {
        this.success = success;
        this.message = message;
        this.name = name;
        this.email = email;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}