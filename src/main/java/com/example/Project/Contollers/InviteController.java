package com.example.Project.Contollers;

import com.example.Project.Models.InviteToken;
import com.example.Project.Models.User;
import com.example.Project.Repository.UserRepository;
import com.example.Project.Repository.CodeFileRepository;
import com.example.Project.Repository.InviteTokenRepository;
import com.example.Project.Services.InviteService;
import com.example.Project.Services.InviteServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class InviteController {

    private final InviteService inviteService;
    private final UserRepository userRepository;
    private final CodeFileRepository codeFileRepository;

    @Autowired
    public InviteController(InviteTokenRepository inviteTokenRepo,
                            UserRepository userRepo,
                            CodeFileRepository codeFileRepo) {
        this.inviteService = new InviteServiceImplementation(inviteTokenRepo);
        this.userRepository = userRepo;
        this.codeFileRepository = codeFileRepo;
    }

    // Matches JS: POST /api/files/{fileId}/invite   { username, role }
    @PostMapping("/api/files/{fileId}/invite")
    public Map<String, String> inviteCollaborator(@PathVariable("fileId") String fileId,
                                                  @RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String roleStr = payload.get("role");

        if (username == null || roleStr == null) {
            throw new IllegalArgumentException("Username and role required");
        }

        User.Role roleEnum = User.Role.valueOf(roleStr);
        String token = inviteService.createInvite(Integer.parseInt(fileId), username, roleEnum);

        Map<String, String> resp = new HashMap<>();
        resp.put("username", username);
        resp.put("role", roleStr);
        resp.put("inviteLink", "http://localhost:8080/api/invite/join?token=" + token);
        return resp;
    }

    // User clicks invite link → returns only their role
    @GetMapping("/api/invite/join")
    public Map<String, String> joinViaInvite(@RequestParam String token) {
        InviteToken invite = inviteService.getInvite(token);

        // Ensure user exists
        User user = userRepository.findByUsername(invite.getUsername()).orElseThrow(() ->
                new IllegalArgumentException("User does not exist")
        );

        // Link user to file with role (insert into code_file_users)
        codeFileRepository.linkUserToFile(invite.getCodeFileId(), user.getId());

        // Clean up token
        inviteService.deleteInvite(token);

        // Return the role only
        Map<String, String> resp = new HashMap<>();
        resp.put("role", invite.getRole().name());
        return resp;
    }
}
