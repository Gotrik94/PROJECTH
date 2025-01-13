package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gestisce le eccezioni IllegalArgumentException.
     *
     * @param ex L'eccezione catturata.
     * @return Una risposta con messaggio di errore e stato HTTP 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Handled IllegalArgumentException: {}", ex.getMessage(), ex);
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
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", ex.getMessage(),
                "code", HttpStatus.INTERNAL_SERVER_ERROR.value()
        ));
    }
}
