package com.example.demo.config;

import com.example.demo.websocket.WebSocketMessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configurazione WebSocket.
 * Configura i gestori e le proprietà per le connessioni WebSocket.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketMessageHandler messageHandler;

    /**
     * Costruttore con dipendenza iniettata.
     *
     * @param messageHandler Handler per la gestione dei messaggi WebSocket.
     */
    public WebSocketConfig(WebSocketMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * Registra i gestori WebSocket e le relative configurazioni.
     *
     * @param registry Registro dei gestori WebSocket.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(messageHandler, "/ws")
                .setAllowedOrigins(getAllowedOrigins()); // Origini consentite configurabili
    }

    /**
     * Ritorna le origini consentite per le connessioni WebSocket.
     * In produzione, restringere le origini per una maggiore sicurezza.
     *
     * @return Array di origini consentite.
     */
    private String[] getAllowedOrigins() {
        // Restituisce "*" solo in ambiente di sviluppo. In produzione specificare i domini consentiti.
        return new String[]{
                "http://localhost:4200",
                "http://localhost:8080"};
    }
}
