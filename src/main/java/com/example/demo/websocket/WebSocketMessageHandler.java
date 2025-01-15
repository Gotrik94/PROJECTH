package com.example.demo.websocket;

import com.example.demo.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestisce le connessioni WebSocket e i messaggi.
 */
@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketMessageHandler.class);

    // Mappa delle sessioni attive, associando un playerId alla sessione WebSocket
    private final ConcurrentHashMap<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    private final JwtUtil jwtUtil;

    /**
     * Costruttore con iniezione delle dipendenze.
     *
     * @param jwtUtil Utility per la gestione dei token JWT.
     */
    public WebSocketMessageHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Metodo chiamato quando una nuova connessione WebSocket viene stabilita.
     *
     * @param session La sessione WebSocket.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer playerId = getPlayerIdFromSession(session);

        if (playerId != null) {
            activeSessions.put(playerId.toString(), session);
            logger.info("WebSocket connection established for player [{}]", playerId);
        } else {
            logger.warn("WebSocket connection rejected: invalid or missing player ID.");
            session.close();
        }
    }

    /**
     * Metodo chiamato quando un messaggio viene ricevuto.
     *
     * @param session La sessione WebSocket.
     * @param message Il messaggio ricevuto.
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        logger.info("Message received from session [{}]: {}", session.getId(), payload);

        // Echo del messaggio al client
        session.sendMessage(new TextMessage("Echo: " + payload));
    }

    /**
     * Metodo chiamato quando una connessione WebSocket viene chiusa.
     *
     * @param session La sessione WebSocket.
     * @param status  Lo stato di chiusura.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        Integer playerId = getPlayerIdFromSession(session);

        if (playerId != null) {
            activeSessions.remove(playerId.toString());
            logger.info("WebSocket connection closed for player [{}]. Status: {}", playerId, status);
        } else {
            logger.warn("WebSocket session closed for unidentified session [{}].", session.getId());
        }
    }

    /**
     * Invia un messaggio a uno specifico giocatore.
     *
     * @param playerId L'ID del giocatore a cui inviare il messaggio.
     * @param message  Il messaggio da inviare.
     */
    public void sendMessageToUser(String playerId, String message) throws Exception {
        WebSocketSession session = activeSessions.get(playerId);

        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
            logger.info("Message sent to player [{}]: {}", playerId, message);
        } else {
            logger.warn("Failed to send message to player [{}]: session not found or closed.", playerId);
        }
    }

    /**
     * Estrae l'ID del giocatore dalla sessione WebSocket.
     *
     * @param session La sessione WebSocket.
     * @return L'ID del giocatore, se presente.
     */
    private Integer getPlayerIdFromSession(WebSocketSession session) {
        try {
            String query = session.getUri().getQuery();

            if (query != null && query.startsWith("token=")) {
                String token = query.substring("token=".length());
                return jwtUtil.extractUserId(token);
            } else {
                logger.warn("No token found in WebSocket session [{}].", session.getId());
            }
        } catch (Exception e) {
            logger.error("Error extracting player ID from session [{}]: {}", session.getId(), e.getMessage());
        }
        return null;
    }
}
