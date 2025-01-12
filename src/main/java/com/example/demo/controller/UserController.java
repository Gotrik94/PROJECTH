package com.example.demo.controller;

import com.example.demo.enums.UserStatus;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Endpoint per la registrazione di un nuovo utente
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username già in uso");
        }
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    // Recupera il profilo dell'utente autenticato
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    // Aggiorna i progressi del giocatore
    @PutMapping("/update-stats")
    public ResponseEntity<?> updateGameStats(@RequestParam String username,
                                             @RequestParam int gamesPlayed,
                                             @RequestParam int gamesWon) {
        userService.updateGameStats(username, gamesPlayed, gamesWon);
        return ResponseEntity.ok("Statistiche aggiornate con successo");
    }
    // Aggiorna lo status nel gioco del giocatore
    @PutMapping("/update-status")
    public ResponseEntity<?> updateUserStatus(@RequestParam String username, @RequestParam UserStatus status) {
        userService.updateUserStatus(username, status);
        return ResponseEntity.ok("Stato aggiornato con successo");
    }

    //  Elimina User
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam String username) {
        return userService.findByUsername(username)
                .map(user -> {
                    userService.deleteUser(user);
                    return ResponseEntity.ok("Utente eliminato con successo");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato"));
    }

}
