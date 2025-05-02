package com.example.Project.Services;

import com.example.Project.Models.InviteToken;
import com.example.Project.Models.User;

public interface InviteService {
    public String createInvite(Integer codeFileId, String username, User.Role role);
    public InviteToken getInvite(String token);
    public void deleteInvite(String token);
}
