package com.example.Project.DTO;

public record ApiResponse(
        boolean success,
        String message,
        String role
) {}