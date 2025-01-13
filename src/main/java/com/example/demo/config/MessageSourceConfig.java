package com.example.demo.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;


/**
 * Configura il MessageSource per la gestione dei messaggi localizzati
 * utilizzando i file messages.properties.
 */
@Configuration
public class MessageSourceConfig {

    /**
     * Definisce un bean MessageSource per caricare i messaggi localizzati dai file properties.
     *
     * @return il MessageSource configurato.
     */
    @Bean
    public MessageSource messageSource() {

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // Specifica il prefisso dei file dei messaggi
        messageSource.setBasename("messages");
        // Specifica un messaggio predefinito se una chiave non viene trovata
        messageSource.setUseCodeAsDefaultMessage(true);

        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}