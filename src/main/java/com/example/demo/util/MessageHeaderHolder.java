package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
public class MessageHeaderHolder {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    @Autowired
    public MessageHeaderHolder(MessageSource messageSource, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    /**
     * Recupera un messaggio localizzato specificando il codice, gli argomenti e il locale.
     *
     * @param code  Codice del messaggio (chiave nei file .properties).
     * @param args  Argomenti da inserire nel messaggio.
     * @param locale Locale per la traduzione.
     * @return Messaggio tradotto.
     */
    public String getMessage(String code, Object[] args, Locale locale) {
        return messageSource.getMessage(code, args, locale);
    }

    /**
     * Recupera un messaggio localizzato utilizzando il locale corrente.
     *
     * @param code Codice del messaggio (chiave nei file .properties).
     * @param args Argomenti da inserire nel messaggio.
     * @return Messaggio tradotto.
     */
    public String getMessage(String code, Object[] args) {
        Locale locale = localeResolver.resolveLocale(null);
        return getMessage(code, args, locale);
    }

    /**
     * Recupera un messaggio localizzato senza argomenti.
     *
     * @param code Codice del messaggio (chiave nei file .properties).
     * @return Messaggio tradotto.
     */
    public String getMessage(String code) {
        return getMessage(code, null);
    }
}
