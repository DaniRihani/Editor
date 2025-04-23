package com.example.Project;

import com.example.Project.Models.CodeFile;
import com.example.Project.Repository.CodeFileRepositoryImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CodeFileRepositoryImplementationTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CodeFileRepositoryImplementation repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_insertsCodeFileAndUserLinks() {
        CodeFile file = new CodeFile();
        file.setFilename("Test.java");
        file.setContent("public class Test {}");
        file.setLanguage("Java");
        file.setUserIds(List.of(1, 2));

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        keyHolder.getKeyList().add(Map.of("GENERATED_KEY", 42));

        doAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Map.of("id", 42));  // Simulate returned key
            return 1;
        }).when(jdbcTemplate).update(any(), any(KeyHolder.class));

        when(jdbcTemplate.batchUpdate(anyString(), anyList())).thenReturn(new int[]{1, 1});

        int id = repository.save(file);

        assertEquals(42, id);
    }


    @Test
    void findByUsername_returnsFilesWithCollaborators() {
        // Arrange
        String username = "john";
        CodeFile mockFile = new CodeFile();
        mockFile.setId(1);
        mockFile.setFilename("Test.java");
        mockFile.setContent("code");
        mockFile.setLanguage("Java");

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(username)))
                .thenReturn(List.of(mockFile));

        when(jdbcTemplate.queryForList(anyString(), eq(Integer.class), eq(1)))
                .thenReturn(List.of(1, 2, 3));

        // Act
        List<CodeFile> result = repository.findByUsername(username);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test.java", result.get(0).getFilename());
        assertEquals(List.of(1, 2, 3), result.get(0).getUserIds());

        verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(username));
        verify(jdbcTemplate).queryForList(anyString(), eq(Integer.class), eq(1));
    }
}
