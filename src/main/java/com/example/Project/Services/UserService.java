package com.example.Project.Services;



import com.example.Project.Models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    String getUserRole(String username, UserDetails currentUser);
    User createUser(String username, String password, User.Role role);
    Optional<User> validateUser(String username, String password);
}
