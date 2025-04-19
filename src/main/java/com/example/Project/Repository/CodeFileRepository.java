package com.example.Project.Repository;


import com.example.Project.Models.CodeFile;
import java.util.List;

public interface CodeFileRepository {
    int save(CodeFile codeFile);
    List<CodeFile> findByUsername(String username);
}
