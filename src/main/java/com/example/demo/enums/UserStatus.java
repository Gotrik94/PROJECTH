package com.example.demo.enums;

public enum UserStatus {
    ONLINE("Online"),
    OFFLINE("Offline"),
    IN_GAME("In Game"),
    AWAY("Away");

    private final String description;

    // Costruttore per associare una descrizione a ogni stato
    UserStatus(String description) {
        this.description = description;
    }

    // Metodo per ottenere la descrizione
    public String getDescription() {
        return description;
    }

    // Metodo statico per ottenere un valore dell'enum da una stringa
    public static UserStatus fromString(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Lo stato non può essere null o vuoto");
        }

        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.name().equalsIgnoreCase(status)) {
                return userStatus;
            }
        }

        throw new IllegalArgumentException("Stato non valido: " + status);
    }
}
