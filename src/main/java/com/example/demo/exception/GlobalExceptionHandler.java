package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * Gestisce globalmente le eccezioni nell'applicazione.
 * Fornisce risposte strutturate per errori comuni.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce le eccezioni IllegalArgumentException.
     *
     * @param ex L'eccezione catturata.
     * @return Una risposta con messaggio di errore e stato HTTP 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", ex.getMessage(),
                "code", HttpStatus.BAD_REQUEST.value()
        ));
    }

    /**
     * Gestisce tutte le altre eccezioni generiche.
     *
     * @param ex L'eccezione catturata.
     * @return Una risposta con messaggio di errore e stato HTTP 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", ex.getMessage(),
                "code", HttpStatus.INTERNAL_SERVER_ERROR.value()
        ));
    }
}
