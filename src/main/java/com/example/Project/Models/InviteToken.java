package com.example.Project.Models;


import java.time.LocalDateTime;

public class InviteToken {
    private String token;
    private Integer codeFileId;
    private String username;
    private User.Role role;
    private LocalDateTime createdAt;

    public InviteToken() {}

    public InviteToken(String token, Integer codeFileId, String username, User.Role role, LocalDateTime createdAt) {
        this.token = token;
        this.codeFileId = codeFileId;
        this.username = username;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Integer getCodeFileId() { return codeFileId; }
    public void setCodeFileId(Integer codeFileId) { this.codeFileId = codeFileId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
