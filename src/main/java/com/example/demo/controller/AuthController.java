package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.MessageHeaderHolder;
import com.example.demo.dtos.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * Controller per la gestione dell'autenticazione degli utenti.
 * Fornisce endpoint per il login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MessageHeaderHolder messageHeaderHolder;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, MessageHeaderHolder messageHeaderHolder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.messageHeaderHolder = messageHeaderHolder;
    }

    /**
     * Effettua il login di un utente e restituisce un token JWT.
     *
     * @param loginRequest Oggetto contenente username e password.
     * @return Risposta con token JWT o errore di autenticazione.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", "Invalid request.",
                            "code", HttpStatus.BAD_REQUEST.value()
                    ));
        }

        try {
            User user = userService.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(
                            messageHeaderHolder.getMessage("auth.credentials.invalid")));

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new BadCredentialsException(
                        messageHeaderHolder.getMessage("auth.credentials.invalid"));
            }

            // Genera il token JWT
            String token = jwtUtil.generateToken(user);

            return ResponseEntity.ok(Map.of(
                    "message", messageHeaderHolder.getMessage("auth.success"),
                    "token", token
            ));
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", ex.getMessage(),
                            "code", HttpStatus.UNAUTHORIZED.value()
                    ));
        }
    }
}

