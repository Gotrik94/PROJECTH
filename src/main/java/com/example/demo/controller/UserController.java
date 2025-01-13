package com.example.demo.controller;

import com.example.demo.enums.UserStatus;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.MessageHeaderHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Controller per la gestione degli utenti, inclusa la registrazione,
 * il recupero del profilo e l'aggiornamento dello stato o delle statistiche.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private MessageHeaderHolder messageHeaderHolder;

    /**
     * Registra un nuovo utente.
     *
     * @param user Dati dell'utente da registrare.
     * @return Risposta con i dettagli dell'utente registrato.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        log.info("Registration attempt for username: {}", user.getUsername());
        // Controllo se l'username è già in uso
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            log.warn("Registration failed: username already exists - {}", user.getUsername());
            String errorMessage = messageHeaderHolder.getMessage("user.user.exists");
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message", errorMessage,
                            "code", HttpStatus.BAD_REQUEST.value()
                    ));
        }
        // Controllo se l'email è già in uso
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            log.warn("Registration failed: email already exists - {}", user.getUsername());
            String errorMessage = messageHeaderHolder.getMessage("user.email.exists");
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message", errorMessage,
                            "code", HttpStatus.BAD_REQUEST.value()
                    ));
        }
        // Registrazione dell'utente
        User registeredUser = userService.registerUser(user);
        log.info("User registered successfully: {}", registeredUser.getUsername());

        // Messaggio di successo
        String successMessage = messageHeaderHolder.getMessage("user.created");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", successMessage,
                        "code", HttpStatus.CREATED.value(),
                        "data", Map.of(
                                "username", registeredUser.getUsername(),
                                "email", registeredUser.getEmail()
                        )
                ));
    }

    /**
     * Recupera il profilo dell'utente autenticato.
     *
     * @param authentication Contesto di autenticazione corrente.
     * @return Profilo dell'utente autenticato.
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        log.info("Profile request for username: {}", username);
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok().body(Map.of(
                        "message", messageHeaderHolder.getMessage("user.profile.found"),
                        "code", HttpStatus.OK.value(),
                        "data", user
                )))
                .orElseGet(() -> {
                    log.warn("Profile not found for username: {}", username);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of(
                                    "message", messageHeaderHolder.getMessage("user.notfound"),
                                    "code", HttpStatus.NOT_FOUND.value()
                            ));
                });
    }

    /**
     * Aggiorna i progressi di gioco del giocatore.
     *
     * @param username Nome utente del giocatore.
     * @param gamesPlayed Numero di partite giocate.
     * @param gamesWon Numero di partite vinte.
     * @return Messaggio di successo.
     */
    @PutMapping("/update-stats")
    public ResponseEntity<?> updateGameStats(@RequestParam String username,
                                             @RequestParam int gamesPlayed,
                                             @RequestParam int gamesWon) {
        log.info("Updating game stats for username: {}, gamesPlayed: {}, gamesWon: {}", username, gamesPlayed, gamesWon);

        userService.updateGameStats(username, gamesPlayed, gamesWon);
        String successMessage = messageHeaderHolder.getMessage("user.stats.updated");
        log.info("Game stats updated successfully for username: {}", username);

        return ResponseEntity.ok(Map.of(
                "message", successMessage,
                "code", HttpStatus.OK.value()
        ));
    }

    /**
     * Aggiorna lo stato di un utente nel gioco.
     *
     * @param username Nome utente del giocatore.
     * @param status Nuovo stato da impostare.
     * @return Messaggio di successo.
     */
    @PutMapping("/update-status")
    public ResponseEntity<?> updateUserStatus(@RequestParam String username, @RequestParam UserStatus status) {
        log.info("Updating status for username: {} to status: {}", username, status);

        userService.updateUserStatus(username, status);
        String successMessage = messageHeaderHolder.getMessage("user.status.updated");
        log.info("Status updated successfully for username: {}", username);

        return ResponseEntity.ok(Map.of(
                "message", successMessage,
                "code", HttpStatus.OK.value()
        ));
    }

    /**
     * Elimina un utente dal sistema.
     *
     * @param username Nome utente da eliminare.
     * @return Messaggio di successo o errore se l'utente non esiste.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam String username) {
        log.info("Delete request for username: {}", username);

        return userService.findByUsername(username)
                .map(user -> {
                    userService.deleteUser(user);
                    String successMessage = messageHeaderHolder.getMessage("user.deleted");
                    log.info("User deleted successfully: {}", username);
                    return ResponseEntity.ok(Map.of(
                            "message", successMessage,
                            "code", HttpStatus.OK.value()
                    ));
                })
                .orElseGet(() -> {
                    log.warn("User not found for deletion: {}", username);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of(
                                    "message", messageHeaderHolder.getMessage("user.notfound"),
                                    "code", HttpStatus.NOT_FOUND.value()
                            ));
                });
    }
}
