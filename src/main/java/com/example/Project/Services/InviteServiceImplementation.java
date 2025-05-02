package com.example.Project.Services;

import com.example.Project.Models.InviteToken;
import com.example.Project.Models.User;
import com.example.Project.Repository.InviteTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class InviteServiceImplementation implements InviteService{
    private final InviteTokenRepository inviteTokenRepository;

    public InviteServiceImplementation(InviteTokenRepository inviteTokenRepository) {
        this.inviteTokenRepository = inviteTokenRepository;
    }

    public String createInvite(Integer codeFileId, String username, User.Role role) {
        String token = UUID.randomUUID().toString();
        InviteToken invite = new InviteToken(token, codeFileId, username, role, LocalDateTime.now());
        inviteTokenRepository.save(invite);
        return token;
    }

    public InviteToken getInvite(String token) {
        return inviteTokenRepository.findByToken(token).orElseThrow(() ->
                new IllegalArgumentException("Invalid invite token")
        );
    }

    public void deleteInvite(String token) {
        inviteTokenRepository.delete(token);
    }
}
