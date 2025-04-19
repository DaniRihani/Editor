package com.example.Project.Services;

import com.example.Project.DTO.CodeUploadRequest;
import com.example.Project.Models.CodeFile;
import java.util.List;

public interface CodeFileService {
    int saveCodeFile(CodeUploadRequest request);
    List<CodeFile> getCodeFilesByUsername(String username);
}
