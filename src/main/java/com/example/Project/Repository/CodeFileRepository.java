package com.example.Project.Repository;

import com.example.Project.Models.CodeFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class CodeFileRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CodeFileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(CodeFile codeFile) {
        // 1) insert into code_files, get generated ID
        String insertFile = """
            INSERT INTO code_files (filename, content, language)
            VALUES (?, ?, ?)
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(insertFile, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, codeFile.getFilename());
            ps.setString(2, codeFile.getContent());
            ps.setString(3, codeFile.getLanguage());
            return ps;
        }, keyHolder);

        int fileId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        // 2) batch insert into join table
        List<Integer> uids = codeFile.getUserIds();
        if (uids != null && !uids.isEmpty()) {
            String joinSql = "INSERT INTO code_file_users (code_file_id, user_id) VALUES (?, ?)";
            List<Object[]> batch = uids.stream()
                    .map(uid -> new Object[]{fileId, uid})
                    .collect(Collectors.toList());
            jdbcTemplate.batchUpdate(joinSql, batch);
        }
        return fileId;
    }

    public List<CodeFile> findByUsername(String username) {
        // join code_files ↔ code_file_users ↔ users
        String sql = """
            SELECT cf.id, cf.filename, cf.content, cf.language
            FROM code_files cf
            JOIN code_file_users cfu ON cf.id = cfu.code_file_id
            JOIN users u ON cfu.user_id = u.id
            WHERE u.username = ?
        """;
        List<CodeFile> files = jdbcTemplate.query(sql, new CodeFileRowMapper(), username);

        // load collaborator IDs for each file
        files.forEach(f -> f.setUserIds(fetchUserIdsForFile(f.getId())));
        return files;
    }

    private List<Integer> fetchUserIdsForFile(int fileId) {
        String sql = "SELECT user_id FROM code_file_users WHERE code_file_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, fileId);
    }

    private static class CodeFileRowMapper implements RowMapper<CodeFile> {
        @Override
        public CodeFile mapRow(ResultSet rs, int rowNum) throws SQLException {
            CodeFile f = new CodeFile();
            f.setId(rs.getInt("id"));
            f.setFilename(rs.getString("filename"));
            f.setContent(rs.getString("content"));
            f.setLanguage(rs.getString("language"));
            return f;
        }
    }
}
