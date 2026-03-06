package com.senai.baseapp.controller.dto;

import java.util.UUID;

public class AuthResponse {

    private final String token;
    private final UUID userId;
    private final String email;

    public AuthResponse(String token, UUID userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
