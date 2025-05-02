package com.example.Project.Repository;


import com.example.Project.Models.InviteToken;
import java.util.Optional;

public interface InviteTokenRepository {
    void save(InviteToken inviteToken);
    Optional<InviteToken> findByToken(String token);
    boolean delete(String token);
}
