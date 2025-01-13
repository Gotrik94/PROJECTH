package com.example.demo.util;

import com.example.demo.enums.UserStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter per gestire la persistenza dell'enum UserStatus come stringa nel database
 * e la sua conversione inversa.
 */
@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {

    private final MessageHeaderHolder messageHeaderHolder;

    /**
     * Costruttore con dipendenza su MessageHeaderHolder.
     *
     * @param messageHeaderHolder Gestore per i messaggi localizzati.
     */
    public UserStatusConverter(MessageHeaderHolder messageHeaderHolder) {
        this.messageHeaderHolder = messageHeaderHolder;
    }

    /**
     * Converte l'enum UserStatus in una stringa da salvare nel database.
     *
     * @param attribute L'attributo UserStatus da convertire.
     * @return La stringa corrispondente.
     */
    @Override
    public String convertToDatabaseColumn(UserStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    /**
     * Converte una stringa del database nell'enum UserStatus.
     *
     * @param dbData La stringa salvata nel database.
     * @return L'enum UserStatus corrispondente.
     * @throws IllegalArgumentException Se il valore non corrisponde a nessuno stato valido.
     */
    @Override
    public UserStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        try {
            return UserStatus.fromString(dbData, messageHeaderHolder);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    messageHeaderHolder.getMessage("error.invalid.status") + ": " + dbData, ex
            );
        }
    }
}
