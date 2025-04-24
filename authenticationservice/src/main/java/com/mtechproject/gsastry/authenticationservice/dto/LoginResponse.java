package com.mtechproject.gsastry.authenticationservice.dto;

import java.util.List;

public class LoginResponse {
    private String username;
    private String token;
    private String role; // Add this field to store roles

    public LoginResponse(String username, String token, String role) {
        this.username = username;
        this.token = token;
        this.role = role;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
