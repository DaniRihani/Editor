package com.example.Project.Models;

import java.util.List;

public class CodeFile {
    private int id;
    private String filename;
    private String content;
    private String language;
    private List<Integer> userIds;  // collaborators

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public List<Integer> getUserIds() { return userIds; }
    public void setUserIds(List<Integer> userIds) { this.userIds = userIds; }
}
