package com.example.Project.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CollaborationWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, List<WebSocketSession>> sessionGroups ;

    @Autowired
    CollaborationWebSocketHandler(){
        sessionGroups = new ConcurrentHashMap<>();
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        URI uri = session.getUri();
        if (uri != null) {
            Map<String, String> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams().toSingleValueMap();
            String sessionId = queryParams.get("sessionId");
            if (sessionId != null) {
                sessionGroups.computeIfAbsent(sessionId, k -> new CopyOnWriteArrayList<>()).add(session);
                System.out.println("New connection with session ID " + sessionId + ": " + session.getId());
            } else {
                session.close();
                System.out.println("Connection rejected: missing session ID");
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        URI uri = session.getUri();
        if (uri != null) {
            Map<String, String> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams().toSingleValueMap();
            String sessionId = queryParams.get("sessionId");
            if (sessionId != null) {
                List<WebSocketSession> sessions = sessionGroups.get(sessionId);
                if (sessions != null) {
                    for (WebSocketSession webSocketSession : sessions) {
                        if (webSocketSession.isOpen()) {
                            webSocketSession.sendMessage(new TextMessage(message.getPayload()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        URI uri = session.getUri();
        if (uri != null) {
            Map<String, String> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams().toSingleValueMap();
            String sessionId = queryParams.get("sessionId");
            if (sessionId != null) {
                List<WebSocketSession> sessions = sessionGroups.get(sessionId);
                if (sessions != null) {
                    sessions.remove(session);
                    if(sessions.isEmpty()){
                        sessionGroups.remove(sessionId);
                    }
                    System.out.println("Connection closed with session ID " + sessionId + ": " + session.getId());
                }
            }
        }
    }
}