package com.example.demo.controller;

import com.example.demo.service.MatchmakingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller per la gestione del matchmaking.
 */
@RestController
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    private final MatchmakingService matchmakingService;

    public MatchmakingController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    /**
     * Aggiunge un giocatore alla coda di matchmaking.
     *
     * @param playerId ID del giocatore.
     * @return Risposta con esito dell'operazione.
     */
    @PostMapping("/join")
    public ResponseEntity<?> joinQueue(@RequestParam String playerId) {
        String message = matchmakingService.addToQueue(playerId);
        return ResponseEntity.ok(Map.of("message", message));
    }

    /**
     * Rimuove un giocatore dalla coda di matchmaking.
     *
     * @param playerId ID del giocatore.
     * @return Risposta con esito dell'operazione.
     */
    @PostMapping("/leave")
    public ResponseEntity<?> leaveQueue(@RequestParam String playerId) {
        String message = matchmakingService.removeFromQueue(playerId);
        return ResponseEntity.ok(Map.of("message", message));
    }
}