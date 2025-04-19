package com.example.Project;



import com.example.Project.Contollers.CodeFileController;
import com.example.Project.DTO.CodeUploadRequest;
import com.example.Project.Models.CodeFile;
import com.example.Project.Services.CodeFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CodeFileControllerTest {

    private CodeFileService codeFileService;
    private CodeFileController codeFileController;

    @BeforeEach
    void setUp() {
        codeFileService = mock(CodeFileService.class);
        codeFileController = new CodeFileController(codeFileService);
    }

    @Test
    void testUploadCodeFile() {
        CodeUploadRequest request = new CodeUploadRequest();
        request.setFilename("Main.java");
        request.setContent("public class Main {}");
        request.setLanguage("java");
        request.setUserIds(List.of(1, 2));

        when(codeFileService.saveCodeFile(request)).thenReturn(123);

        ResponseEntity<String> response = codeFileController.upload(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Code file saved with ID: 123", response.getBody());
    }

    @Test
    void testGetByUser_ReturnsFiles() {
        CodeFile file = new CodeFile();
        file.setId(1);
        file.setFilename("script.py");
        file.setContent("print('hello')");
        file.setLanguage("python");
        file.setUserIds(List.of(1, 2));

        when(codeFileService.getCodeFilesByUsername("alice"))
                .thenReturn(List.of(file));

        ResponseEntity<List<CodeFile>> response = codeFileController.getByUser("alice");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("script.py", response.getBody().get(0).getFilename());
    }

    @Test
    void testGetByUser_ReturnsNoContent() {
        when(codeFileService.getCodeFilesByUsername("bob"))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<CodeFile>> response = codeFileController.getByUser("bob");

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }
}

