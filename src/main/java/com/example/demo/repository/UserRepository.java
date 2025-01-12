package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Metodo per trovare un utente tramite username
    Optional<User> findByUsername(String username);

    // Metodo per trovare un utente tramite email (se necessario)
    Optional<User> findByEmail(String email);

    // Metodo opzionale per contare gli utenti con un determinato ruolo
    long countByRole(String role);
}
