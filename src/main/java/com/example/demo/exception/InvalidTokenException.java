package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.util.MessageHeaderHolder;

/**
 * Eccezione lanciata quando il token JWT fornito è invalido.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends RuntimeException {

    private static final String DEFAULT_MESSAGE_KEY = "auth.invalid";

    @Autowired
    public InvalidTokenException(MessageHeaderHolder messageHeaderHolder) {
        super(messageHeaderHolder.getMessage(DEFAULT_MESSAGE_KEY));
    }

    /**
     * @param message Messaggio di errore.
     */
    public InvalidTokenException(String message) {
        super(message);
    }
}
