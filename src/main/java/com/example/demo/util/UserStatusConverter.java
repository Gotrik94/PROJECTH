package com.example.demo.util; // Oppure com.example.demo.converter


import com.example.demo.enums.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {

    @Override
    public String convertToDatabaseColumn(UserStatus status) {
        return status != null ? status.name() : null; // Salva il nome dell'enum
    }

    @Override
    public UserStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return UserStatus.fromString(dbData); // Usa il metodo `fromString` dell'enum
    }
}