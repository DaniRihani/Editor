package com.example.Project.Contollers;

import com.example.Project.Models.LoginRequest;
import com.example.Project.Models.User;
import com.example.Project.Services.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserServiceImplementation userService;

    @Autowired
    public AuthController(UserServiceImplementation userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userService
                .validateUser(loginRequest.getUsername(), loginRequest.getPassword());
        System.out.println(userOpt);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String role = user.getRole().name();
            Integer userId = user.getId();

            // Build a mutable map so we can put more than 10 entries if needed
            Map<String,Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("role",    role);
            resp.put("userId",  userId);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resp);
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "success", false,
                            "message", "Invalid credentials"
                    ));
        }
    }
}
