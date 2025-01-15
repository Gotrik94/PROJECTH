package com.example.demo.websocket;

import com.example.demo.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestisce le connessioni WebSocket e i messaggi.
 */
@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketMessageHandler.class);

    @Autowired
    private JwtUtil jwtUtil;

    // Mappa per tracciare le sessioni WebSocket attive
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Estrai il token dalla query string
        String token = session.getUri().getQuery(); // Assumiamo ?token=...

        try {
            // Validazione del token
            if (token == null || token.isEmpty() || !token.startsWith("token=")) {
                throw new IllegalArgumentException("Missing or invalid token.");
            }

            token = token.substring("token=".length());
            String username = jwtUtil.validateTokenAndGetUsername(token); // Metodo che valida il token e restituisce l'utente
            logger.info("WebSocket connection established for user: {}", username);

            // Memorizziamo la sessione solo se il token è valido
            sessions.put(session.getId(), session);
        } catch (Exception e) {
            logger.error("Invalid token for WebSocket connection. Reason: {}", e.getMessage());
            session.close(); // Chiude immediatamente la connessione
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info("Message received from [{}]: {}", session.getId(), message.getPayload());

        // Rispondiamo al client con un semplice messaggio di echo
        try {
            session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
        } catch (Exception e) {
            logger.error("Error while sending message to client [{}].", session.getId(), e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        logger.info("WebSocket connection closed. Session ID: {}, Status: {}", session.getId(), status);
        String token = session.getUri().getQuery(); // Legge il token dai parametri di query

        try {
            String username = jwtUtil.extractUsername(token);
            logger.info("WebSocket connection established for user: {}", username);
            sessions.put(session.getId(), session);
        } catch (Exception e) {
            logger.error("Invalid token for WebSocket connection.");
            session.close(); // Chiude la connessione se il token è invalido
        }
        sessions.remove(session.getId());
    }
}
