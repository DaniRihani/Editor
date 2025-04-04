package com.example.Project.DTO;

public class AuthResponse {
    private final String message;

    private final String userName;
    public AuthResponse(String message, String userName) {
        this.message = message;
        this.userName=userName;
    }
    // Getters
    public String getMessage() { return message; }
    public String getUserName() { return userName; }

}