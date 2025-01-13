package com.example.demo.enums;

import com.example.demo.util.MessageHeaderHolder;

/**
 * Enum che rappresenta i possibili stati di un utente nel sistema.
 */
public enum UserStatus {

    /**
     * L'utente è online.
     */
    ONLINE("user.status.online"),

    /**
     * L'utente è offline.
     */
    OFFLINE("user.status.offline"),

    /**
     * L'utente è attualmente in una partita.
     */
    IN_GAME("user.status.inGame"),

    /**
     * L'utente è inattivo o lontano.
     */
    AWAY("user.status.away");

    /**
     * Chiave del messaggio localizzato per lo stato dell'utente.
     */
    private final String messageKey;

    /**
     * Costruttore per associare una chiave di messaggio a ogni stato.
     *
     * @param messageKey La chiave del messaggio localizzato.
     */
    UserStatus(String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * Restituisce la descrizione dello stato basata sulla chiave del messaggio.
     *
     * @param messageHeaderHolder Il gestore dei messaggi localizzati.
     * @return La descrizione dello stato localizzata.
     */
    public String getDescription(MessageHeaderHolder messageHeaderHolder) {
        return messageHeaderHolder.getMessage(messageKey);
    }

    /**
     * Restituisce lo stato corrispondente a una stringa specificata.
     *
     * @param status La stringa da confrontare con i nomi degli stati.
     * @param messageHeaderHolder Il gestore dei messaggi localizzati.
     * @return Lo stato corrispondente alla stringa.
     * @throws IllegalArgumentException Se la stringa non corrisponde a nessuno stato.
     */
    public static UserStatus fromString(String status, MessageHeaderHolder messageHeaderHolder) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.name().equalsIgnoreCase(status)) {
                return userStatus;
            }
        }
        throw new IllegalArgumentException(messageHeaderHolder.getMessage("error.invalid.status") + ": " + status);
    }
}
