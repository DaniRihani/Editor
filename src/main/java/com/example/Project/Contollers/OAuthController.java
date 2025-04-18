package com.example.Project.Contollers;

import com.example.Project.DTO.AuthResponse;
import com.example.Project.DTO.ErrorResponse;
import com.example.Project.DTO.GoogleTokenRequest;
import com.example.Project.Execptions.InvalidTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@RestController
@RequestMapping("/api")
public class OAuthController {

    private final String googleClientId;

    public OAuthController(
            @Value("${security.oauth2.google.client-id}") String googleClientId
    ) {
        this.googleClientId = googleClientId;
    }

    @PostMapping("/login/oauth2/code/google")
    public ResponseEntity<?> handleGoogleLogin(@RequestBody GoogleTokenRequest request) {
        if (request.getToken() == null || request.getToken().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Missing Google token"));
        }

        try {
            GoogleIdToken.Payload payload = verifyGoogleToken(request.getToken());
            String username = extractUsername(payload.getEmail());

            return ResponseEntity.ok().body(new AuthResponse(
                    "Login successful",
                    username
            ));

        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid Google token"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Authentication failed: " + e.getMessage()));
        }
    }

    private String extractUsername(String email) {
        return email.split("@")[0];
    }

    private GoogleIdToken.Payload verifyGoogleToken(String idToken)
            throws GeneralSecurityException, IOException, InvalidTokenException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);
        if (googleIdToken == null) {
            throw new InvalidTokenException("Invalid ID token");
        }
        return googleIdToken.getPayload();
    }
}