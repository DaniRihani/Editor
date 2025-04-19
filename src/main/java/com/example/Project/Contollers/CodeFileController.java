package com.example.Project.Contollers;

import com.example.Project.DTO.CodeUploadRequest;
import com.example.Project.Models.CodeFile;
import com.example.Project.Services.CodeFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/code")
public class CodeFileController {

    private final CodeFileService codeFileService;

    @Autowired
    public CodeFileController(CodeFileService codeFileService) {
        this.codeFileService = codeFileService;
    }

    @PostMapping
    public ResponseEntity<String> upload(@RequestBody CodeUploadRequest req) {
        int id = codeFileService.saveCodeFile(req);
        return ResponseEntity.ok("Code file saved with ID: " + id);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<CodeFile>> getByUser(@PathVariable("username") String username) {
        List<CodeFile> list = codeFileService.getCodeFilesByUsername(username);
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}