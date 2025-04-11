package com.example.Project.Services;


import com.example.Project.Repository.UserRepository;
import com.example.Project.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserRole(String username, UserDetails currentUser) {
        // Authorization check
        if (!currentUser.getUsername().equals(username) &&
                !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new SecurityException("Access denied");
        }

        return userRepository.findByUsername(username)
                .map(user -> user.getRole().name())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(String username, String password, User.Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    public Optional<User> validateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }
}