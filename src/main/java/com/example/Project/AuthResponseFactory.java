package com.example.Project;


import com.example.Project.DTO.AuthResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthResponseFactory {
    public AuthResponse createSuccessResponse(String email) {
        return new AuthResponse("Login successful", email);
    }

    public AuthResponse createFailureResponse(String error) {
        return new AuthResponse("Login failed: " + error, null);
    }
}