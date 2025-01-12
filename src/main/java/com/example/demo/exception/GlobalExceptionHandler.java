package com.example.demo.exception;

import com.example.demo.util.MessageHeaderHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageHeaderHolder messageHeaderHolder;

    @Autowired
    public GlobalExceptionHandler(MessageHeaderHolder messageHeaderHolder) {
        this.messageHeaderHolder = messageHeaderHolder;
    }

    /**
     * Gestisce le eccezioni di token JWT invalido.
     *
     * @param ex Eccezione lanciata quando il token JWT è invalido.
     * @return Risposta con codice 401 e messaggio di errore.
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidToken(InvalidTokenException ex) {
        String errorMessage = messageHeaderHolder.getMessage("auth.invalid");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", errorMessage,
                        "code", HttpStatus.UNAUTHORIZED.value()
                ));
    }

    // Puoi aggiungere altri metodi per gestire altre eccezioni
}
