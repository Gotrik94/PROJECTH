package com.example.demo.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

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
