package com.example.Project;


import com.example.Project.Models.User;
import com.example.Project.Repository.UserRepositoryImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryImplementationTest {

    private JdbcTemplate jdbcTemplate;
    private UserRepositoryImplementation repository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        repository = new UserRepositoryImplementation(jdbcTemplate);
    }

    @Test
    void findById_returnsUser() {
        User user = mockUser();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1)))
                .thenReturn(List.of(user));

        Optional<User> result = repository.findById(1);

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void findByUsername_returnsUser() {
        User user = mockUser();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq("john")))
                .thenReturn(List.of(user));
        Optional<User> result = repository.findByUsername("john");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void update_updatesUserSuccessfully() {
        User user = mockUser();
        user.setId(1);
        when(jdbcTemplate.update(anyString(), any(), any(), any(), eq(1))).thenReturn(1);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1)))
                .thenReturn(List.of(user));

        User updated = repository.update(user);

        assertEquals("john", updated.getUsername());
    }

    @Test
    void delete_deletesSuccessfully() {
        when(jdbcTemplate.update(anyString(), eq(1))).thenReturn(1);

        boolean deleted = repository.delete(1);

        assertTrue(deleted);
    }

    @Test
    void existsByUsername_returnsTrue() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("john"))).thenReturn(1);

        assertTrue(repository.existsByUsername("john"));
    }

    @Test
    void existsByUsername_returnsFalse() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("jane"))).thenReturn(0);

        assertFalse(repository.existsByUsername("jane"));
    }

    private User mockUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("john");
        user.setPassword("secret");
        user.setRole(User.Role.editor);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
