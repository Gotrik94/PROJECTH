package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test per il controller MatchmakingController.
 * Verifica il funzionamento degli endpoint /join e /leave.
 */
@SpringBootTest(properties = {
        "server.port=9090" // Esegui l'applicazione di test su una porta diversa
})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class MatchmakingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Testa l'aggiunta di un giocatore alla coda di matchmaking con successo.
     * Verifica che l'endpoint /join risponda correttamente.
     */
    @Test
    public void testJoinQueueSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/matchmaking/join")
                        .param("playerId", "player123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("You are already in the matchmaking queue."));
    }

    /**
     * Testa il comportamento quando un giocatore tenta di unirsi alla coda più di una volta.
     * Verifica che l'endpoint /join restituisca un messaggio appropriato.
     */
    @Test
    public void testJoinQueueAlreadyInQueue() throws Exception {
        // Arrange
        mockMvc.perform(post("/api/matchmaking/join")
                        .param("playerId", "player123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Act & Assert
        mockMvc.perform(post("/api/matchmaking/join")
                        .param("playerId", "player123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("You are already in the matchmaking queue."));
    }

    /**
     * Testa il comportamento quando il parametro playerId è mancante.
     * Verifica che l'endpoint /join restituisca un errore 400.
     */
    @Test
    public void testJoinQueueMissingPlayerId() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/matchmaking/join")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Required request parameter 'playerId' for method parameter type String is not present"));
    }

    /**
     * Testa la rimozione di un giocatore dalla coda di matchmaking con successo.
     * Verifica che l'endpoint /leave risponda correttamente.
     */
    @Test
    public void testLeaveQueueSuccess() throws Exception {
        // Arrange
        mockMvc.perform(post("/api/matchmaking/join")
                        .param("playerId", "player123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Act & Assert
        mockMvc.perform(post("/api/matchmaking/leave")
                        .param("playerId", "player123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("You have been removed from the matchmaking queue."));
    }

    /**
     * Testa il comportamento quando un giocatore non è presente nella coda e tenta di uscire.
     * Verifica che l'endpoint /leave restituisca un messaggio appropriato.
     */
    @Test
    public void testLeaveQueueNotInQueue() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/matchmaking/leave")
                        .param("playerId", "player123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("You have been removed from the matchmaking queue."));
    }

    /**
     * Testa il comportamento quando il parametro playerId è mancante durante la rimozione.
     * Verifica che l'endpoint /leave restituisca un errore 400.
     */
    @Test
    public void testLeaveQueueMissingPlayerId() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/matchmaking/leave")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Required request parameter 'playerId' for method parameter type String is not present"));
    }
}
