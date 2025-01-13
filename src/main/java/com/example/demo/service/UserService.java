package com.example.demo.service;

import com.example.demo.enums.UserStatus;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servizio per la gestione degli utenti.
 * Fornisce metodi per la registrazione, l'aggiornamento dello stato e delle statistiche,
 * e la gestione delle informazioni degli utenti.
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Trova un utente tramite username.
     *
     * @param username Nome utente da cercare.
     * @return Optional contenente l'utente se trovato.
     */
    public Optional<User> findByUsername(String username) {
        log.debug("Searching for user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Trova un utente tramite email.
     *
     * @param email Email da cercare.
     * @return Optional contenente l'utente se trovato.
     */
    public Optional<User> findByEmail(String email) {
        log.debug("Searching for user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * Registra un nuovo utente.
     * Codifica la password, imposta la data di registrazione e salva l'utente.
     *
     * @param user Dati dell'utente da registrare.
     * @return Utente registrato.
     */
    public User registerUser(User user) {
        log.info("Registering user with username: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());
        return savedUser;
    }

    /**
     * Aggiorna la data di ultimo login per un utente.
     *
     * @param username Nome utente per il quale aggiornare l'ultimo login.
     */
    public void updateLastLogin(String username) {
        log.info("Updating last login for username: {}", username);
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            log.info("Last login updated for username: {}", username);
        });
    }

    /**
     * Aggiorna i progressi di gioco del giocatore.
     *
     * @param username Nome utente del giocatore.
     * @param gamesPlayed Numero di partite giocate.
     * @param gamesWon Numero di partite vinte.
     */
    public void updateGameStats(String username, int gamesPlayed, int gamesWon) {
        log.info("Updating game stats for username: {}, gamesPlayed: {}, gamesWon: {}", username, gamesPlayed, gamesWon);
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setGamesPlayed(gamesPlayed);
            user.setGamesWon(gamesWon);
            userRepository.save(user);
            log.info("Game stats updated successfully for username: {}", username);
        });
    }

    /**
     * Aggiorna lo stato di un utente nel gioco.
     *
     * @param username Nome utente del giocatore.
     * @param status Nuovo stato da impostare.
     */
    public void updateUserStatus(String username, UserStatus status) {
        log.info("Updating status for username: {} to status: {}", username, status);
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setStatus(status);
            userRepository.save(user);
            log.info("Status updated successfully for username: {}", username);
        });
    }

    /**
     * Elimina un utente dal sistema.
     *
     * @param user Utente da eliminare.
     */
    public void deleteUser(User user) {
        log.info("Deleting user: {}", user.getUsername());
        userRepository.delete(user);
        log.info("User deleted successfully: {}", user.getUsername());
    }
}
