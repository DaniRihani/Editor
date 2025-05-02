package com.example.Project.Repository;


import com.example.Project.Models.InviteToken;
import com.example.Project.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
@Repository
public class InviteTokenRepositoryImplementation implements InviteTokenRepository {

    private final DataSource dataSource;

    @Autowired
    public InviteTokenRepositoryImplementation(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(InviteToken inviteToken) {
        String sql = """
            INSERT INTO invite_tokens (token, code_file_id, username, role, created_at)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inviteToken.getToken());
            stmt.setInt(2, inviteToken.getCodeFileId());
            stmt.setString(3, inviteToken.getUsername());
            stmt.setString(4, inviteToken.getRole().name());
            stmt.setTimestamp(5, Timestamp.valueOf(inviteToken.getCreatedAt()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save invite token", e);
        }
    }

    @Override
    public Optional<InviteToken> findByToken(String token) {
        String sql = """
            SELECT token, code_file_id, username, role, created_at
            FROM invite_tokens
            WHERE token = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    InviteToken invite = new InviteToken();
                    invite.setToken(rs.getString("token"));
                    invite.setCodeFileId(rs.getInt("code_file_id"));
                    invite.setUsername(rs.getString("username"));
                    invite.setRole(User.Role.valueOf(rs.getString("role")));
                    invite.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                    return Optional.of(invite);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find invite token", e);
        }
    }

    @Override
    public boolean delete(String token) {
        String sql = "DELETE FROM invite_tokens WHERE token = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete invite token", e);
        }
    }
}

