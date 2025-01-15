package com.example.demo.service;

import com.example.demo.websocket.WebSocketMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Servizio per la gestione del matchmaking.
 */
@Service
public class MatchmakingService {

    private static final Logger logger = LoggerFactory.getLogger(MatchmakingService.class);

    // Necessario per gestire le notifiche da mandare ai giocatori
    @Autowired
    private WebSocketMessageHandler webSocketHandler;

    // Coda per gestire i giocatori in attesa di un match
    private final Queue<String> matchmakingQueue = new ConcurrentLinkedQueue<>();

    /**
     * Aggiunge un giocatore alla coda di matchmaking.
     *
     * @param playerId ID del giocatore (ad esempio, username o ID univoco).
     * @return Messaggio di conferma.
     */
    public String addToQueue(String playerId) {
        if (matchmakingQueue.contains(playerId)) {
            logger.info("Player [{}] is already in the matchmaking queue.", playerId);
            return "You are already in the matchmaking queue.";
        }

        matchmakingQueue.add(playerId);
        logger.info("Player [{}] added to the matchmaking queue.", playerId);

        // Prova a creare un match
        tryToMatch();

        return "You have been added to the matchmaking queue.";
    }

    /**
     * Rimuove un giocatore dalla coda di matchmaking.
     *
     * @param playerId ID del giocatore.
     * @return Messaggio di conferma.
     */
    public String removeFromQueue(String playerId) {
        if (matchmakingQueue.remove(playerId)) {
            logger.info("Player [{}] removed from the matchmaking queue.", playerId);
            return "You have been removed from the matchmaking queue.";
        } else {
            logger.info("Player [{}] is not in the matchmaking queue.", playerId);
            return "You are not in the matchmaking queue.";
        }
    }

    /**
     * Prova a creare un match accoppiando due giocatori.
     */
    private void tryToMatch() {
        while (matchmakingQueue.size() >= 2) {
            String player1 = matchmakingQueue.poll();
            String player2 = matchmakingQueue.poll();

            if (player1 != null && player2 != null) {
                logger.info("Match created between [{}] and [{}].", player1, player2);
                // Qui possiamo creare una sessione di gioco o notificare i giocatori
                notifyPlayers(player1, player2);
            }
        }
    }

    /**
     * Notifica i giocatori accoppiati.
     *
     * @param player1 Giocatore 1.
     * @param player2 Giocatore 2.
     */
    private void notifyPlayers(String player1, String player2) {
        try {
            webSocketHandler.sendMessageToUser(player1, "Match found! Your opponent is " + player2);
            webSocketHandler.sendMessageToUser(player2, "Match found! Your opponent is " + player1);
        } catch (Exception e) {
            logger.error("Error notifying players: {}", e.getMessage());
        }
    }
}