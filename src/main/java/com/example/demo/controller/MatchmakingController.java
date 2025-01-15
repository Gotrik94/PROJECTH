package com.example.demo.controller;

import com.example.demo.service.MatchmakingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller per la gestione del matchmaking.
 * Fornisce endpoint per aggiungere o rimuovere giocatori dalla coda.
 */
@RestController
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    private final MatchmakingService matchmakingService;

    /**
     * Costruttore con dipendenze iniettate.
     *
     * @param matchmakingService Servizio per la gestione del matchmaking.
     */
    public MatchmakingController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    /**
     * Endpoint per aggiungere un giocatore alla coda di matchmaking.
     *
     * @param playerId ID del giocatore, passato come parametro di richiesta.
     * @return Risposta con il risultato dell'operazione.
     */
    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> joinQueue(@RequestParam String playerId) {
        if (playerId == null || playerId.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Player ID cannot be null or empty."));
        }

        String message = matchmakingService.addToQueue(playerId);
        return ResponseEntity.ok(Map.of("message", message));
    }

    /**
     * Endpoint per rimuovere un giocatore dalla coda di matchmaking.
     *
     * @param playerId ID del giocatore, passato come parametro di richiesta.
     * @return Risposta con il risultato dell'operazione.
     */
    @PostMapping("/leave")
    public ResponseEntity<Map<String, String>> leaveQueue(@RequestParam String playerId) {
        if (playerId == null || playerId.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Player ID cannot be null or empty."));
        }

        String message = matchmakingService.removeFromQueue(playerId);
        return ResponseEntity.ok(Map.of("message", message));
    }
}
