package com.example.demo.service;

import com.example.demo.enums.UserStatus;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Trova un utente per username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Registra un nuovo utente
    public User registerUser(User user) {
        // Codifica la password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Imposta la data di registrazione e salva
        user.setRegistrationDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    // Aggiorna la data di ultimo login
    public void updateLastLogin(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    // Aggiorna i progressi del giocatore
    public void updateGameStats(String username, int gamesPlayed, int gamesWon) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setGamesPlayed(gamesPlayed);
            user.setGamesWon(gamesWon);
            userRepository.save(user);
        });
    }
    // Aggiorna Status del giocatore
    public void updateUserStatus(String username, UserStatus status) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setStatus(status);
            userRepository.save(user);
        });
    }

    //  Elimina User
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
