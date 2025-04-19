package com.example.Project.Repository;

import com.example.Project.Models.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Integer id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    User update(User user);
    boolean delete(Integer id);
    boolean existsByUsername(String username);
}