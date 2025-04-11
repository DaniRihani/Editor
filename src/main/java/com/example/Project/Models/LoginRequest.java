package com.example.Project.Models;

public class LoginRequest {
    private String username;
    private String password;
//    private String token; // Used for OAuth login


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   /* public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }*/
}
