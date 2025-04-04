package com.example.Project.configs;

import com.example.Project.Socket.CollaborationWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CollaborationWebSocketHandler collaborationHandler;

    @Autowired
    public WebSocketConfig(CollaborationWebSocketHandler collaborationHandler) {
        this.collaborationHandler = collaborationHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(collaborationHandler, "/collaboration")
                .setAllowedOriginPatterns("*"); // Use setAllowedOriginPatterns instead of setAllowedOrigins
    }
}
