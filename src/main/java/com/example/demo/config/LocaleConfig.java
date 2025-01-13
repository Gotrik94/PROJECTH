package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Configura il supporto per la localizzazione, consentendo di cambiare lingua
 * tramite l'intestazione Accept-Language o un parametro di query specifico.
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * Configura il LocaleResolver per risolvere la lingua basandosi sull'intestazione Accept-Language.
     *
     * @return l'istanza di LocaleResolver configurata.
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }

    /**
     * Configura l'intercettore che consente di cambiare lingua tramite un parametro di query.
     *
     * @return l'istanza di LocaleChangeInterceptor configurata.
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        // Specifica il parametro di query per cambiare la lingua
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Registra l'intercettore per gestire i cambi di lingua nelle richieste HTTP.
     *
     * @param registry il registro degli intercettori.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
