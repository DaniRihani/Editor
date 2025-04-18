package com.example.Project.Contollers;

import com.example.Project.Models.CodeFile;
import com.example.Project.Repository.CodeFileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.example.Project.DTO.CodeUploadRequest;


@RestController
@RequestMapping("/api/code")
public class CodeFileController {

    private final CodeFileRepository repo;

    @Autowired
    public CodeFileController(CodeFileRepository repo) {
        this.repo = repo;
    }

    // Upload and link to multiple users
    @PostMapping
    public ResponseEntity<String> upload(@RequestBody CodeUploadRequest req) {
        CodeFile cf = new CodeFile();
        cf.setFilename(req.getFilename());
        cf.setContent(req.getContent());
        cf.setLanguage(req.getLanguage());
        cf.setUserIds(req.getUserIds());

        int id = repo.save(cf);
        return ResponseEntity.ok("Code file saved with ID: " + id);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<CodeFile>> getByUser(
            @PathVariable("username") String username
    ) {
        List<CodeFile> list = repo.findByUsername(username);
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}


