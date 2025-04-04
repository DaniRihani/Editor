package com.example.Project.Contollers;

import com.example.Project.Models.LoginRequest;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:8000")
@RestController
@RequestMapping("/api")
public class LoginController {

    @PostMapping("/login")
    public String handleLogin(@RequestBody LoginRequest loginRequest) {
        // Handle normal login logic (e.g., validate username and password)

        return "User logged in successfully";
    }
}
