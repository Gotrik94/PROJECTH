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

import java.util.Map;


/**
 * Controller per la gestione degli utenti, inclusa la registrazione,
 * il recupero del profilo e l'aggiornamento dello stato o delle statistiche.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

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
        // Controllo se l'username è già in uso
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            String errorMessage = messageHeaderHolder.getMessage("user.user.exists");
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message", errorMessage,
                            "code", HttpStatus.BAD_REQUEST.value()
                    ));
        }
        // Controllo se l'email è già in uso
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            String errorMessage = messageHeaderHolder.getMessage("user.email.exists");
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message", errorMessage,
                            "code", HttpStatus.BAD_REQUEST.value()
                    ));
        }
        // Registrazione dell'utente
        User registeredUser = userService.registerUser(user);

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
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok().body(Map.of(
                        "message", messageHeaderHolder.getMessage("user.profile.found"),
                        "code", HttpStatus.OK.value(),
                        "data", user
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "message", messageHeaderHolder.getMessage("user.notfound"),
                                "code", HttpStatus.NOT_FOUND.value()
                        )));
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
        userService.updateGameStats(username, gamesPlayed, gamesWon);
        String successMessage = messageHeaderHolder.getMessage("user.stats.updated");
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
        userService.updateUserStatus(username, status);
        String successMessage = messageHeaderHolder.getMessage("user.status.updated");
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
        return userService.findByUsername(username)
                .map(user -> {
                    userService.deleteUser(user);
                    String successMessage = messageHeaderHolder.getMessage("user.deleted");
                    return ResponseEntity.ok(Map.of(
                            "message", successMessage,
                            "code", HttpStatus.OK.value()
                    ));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "message", messageHeaderHolder.getMessage("user.notfound"),
                                "code", HttpStatus.NOT_FOUND.value()
                        )));
    }
}
