package com.example.Project.Services;



import com.example.Project.DTO.CodeUploadRequest;
import com.example.Project.Models.CodeFile;
import com.example.Project.Repository.CodeFileRepositoryImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CodeFileServiceImplementation implements CodeFileService{

    private final CodeFileRepositoryImplementation codeFileRepository;

    @Autowired
    public CodeFileServiceImplementation(CodeFileRepositoryImplementation codeFileRepository) {
        this.codeFileRepository = codeFileRepository;
    }

    @Transactional
    public int saveCodeFile(CodeUploadRequest request) {
        CodeFile codeFile = new CodeFile();
        codeFile.setFilename(request.getFilename());
        codeFile.setContent(request.getContent());
        codeFile.setLanguage(request.getLanguage());
        codeFile.setUserIds(request.getUserIds());
        return codeFileRepository.save(codeFile);
    }

    public List<CodeFile> getCodeFilesByUsername(String username) {
        return codeFileRepository.findByUsername(username);
    }
}
