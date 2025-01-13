package com.example.demo.service;

import com.example.demo.enums.UserStatus;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servizio per la gestione degli utenti.
 * Fornisce metodi per la registrazione, l'aggiornamento dello stato e delle statistiche,
 * e la gestione delle informazioni degli utenti.
 */
@Service
public class UserService {

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
        return userRepository.findByUsername(username);
    }

    /**
     * Trova un utente tramite email.
     *
     * @param email Email da cercare.
     * @return Optional contenente l'utente se trovato.
     */
    public Optional<User> findByEmail(String email) {
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
        // Codifica la password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Imposta la data di registrazione e salva
        user.setRegistrationDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * Aggiorna la data di ultimo login per un utente.
     *
     * @param username Nome utente per il quale aggiornare l'ultimo login.
     */
    public void updateLastLogin(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
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
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setGamesPlayed(gamesPlayed);
            user.setGamesWon(gamesWon);
            userRepository.save(user);
        });
    }

    /**
     * Aggiorna lo stato di un utente nel gioco.
     *
     * @param username Nome utente del giocatore.
     * @param status Nuovo stato da impostare.
     */
    public void updateUserStatus(String username, UserStatus status) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setStatus(status);
            userRepository.save(user);
        });
    }

    /**
     * Elimina un utente dal sistema.
     *
     * @param user Utente da eliminare.
     */
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
