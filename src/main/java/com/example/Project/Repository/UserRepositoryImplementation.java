package com.example.Project.Repository;

import com.example.Project.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImplementation implements UserRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return user;
    };

    // Create
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users (username, password, role) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name());
            return ps;
        }, keyHolder);

        // Grab the newly-generated PK
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated user ID");
        }
        int newId = key.intValue();

        // Query back the full user row
        return findById(newId)
                .orElseThrow(() -> new IllegalStateException("Inserted user not found"));
    }

    // Read
    public Optional<User> findById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, userRowMapper, id)
                .stream()
                .findFirst();
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.query(sql, userRowMapper, username)
                .stream()
                .findFirst();
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    // Update
    public User update(User user) {
        // 1) Execute the UPDATE
        String sql = """
        UPDATE users 
        SET username = ?, password = ?, role = ?
        WHERE id = ?
        """;
        int rows = jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getRole().name(),
                user.getId()
        );
        if (rows == 0) {
            throw new IllegalArgumentException("No user found with ID " + user.getId());
        }

        // 2) Re‑query for the full record
        return findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("Updated user not found"));
    }

    // Delete
    public boolean delete(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    // Additional methods
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }
}