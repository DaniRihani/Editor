package com.example.Project.configs;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OAuthConfig {
    @Value("${security.oauth2.google.client-id}")
    private String clientId;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        ).setAudience(Collections.singletonList(clientId))
                .build();
    }
}
