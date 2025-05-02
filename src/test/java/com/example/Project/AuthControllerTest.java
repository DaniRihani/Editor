package com.example.Project;

import com.example.Project.Contollers.AuthController;
import com.example.Project.Models.LoginRequest;
import com.example.Project.Models.User;
import com.example.Project.Services.UserServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.example.Project.Models.User.Role;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private UserServiceImplementation userService;
    private AuthController authController;

    @BeforeEach
    void setup() {
        userService = mock(UserServiceImplementation.class);
        authController = new AuthController(userService);
    }

    @Test
    void testLogin_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john");
        loginRequest.setPassword("secret");

        User mockUser = User.builder()
                .id(1)
                .username("john")
                .password("secret")
                .role(Role.admin)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userService.validateUser("john", "secret")).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(Map.class, response.getBody());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertTrue((Boolean) body.get("success"));
        assertEquals("admin", body.get("role"));
        assertEquals(1, body.get("userId"));
    }

    @Test
    void testLogin_Failure() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("wrong");
        loginRequest.setPassword("badpass");

        when(userService.validateUser("wrong", "badpass")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        assertInstanceOf(Map.class, response.getBody());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) body.get("success"));
        assertEquals("Invalid credentials", body.get("message"));
    }
}
