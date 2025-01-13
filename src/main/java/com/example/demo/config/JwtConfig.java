package com.example.demo.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * Configurazione per la gestione dei parametri JWT utilizzati per la sicurezza.
 */
@Configuration
@Getter
public class JwtConfig {

    /**
     * Chiave segreta utilizzata per firmare i token JWT.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Tempo di scadenza dei token JWT in millisecondi.
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Emittente dei token JWT.
     */
    @Value("${jwt.issuer}")
    private String issuer;

    /**
     * Tolleranza di sincronizzazione per la validazione dei token JWT.
     */
    @Value("${jwt.clock-skew}")
    private Long clockSkew;

    /**
     * Algoritmo di firma dei token JWT.
     */
    @Value("${jwt.algorithm}")
    private String algorithm;

    public String getSecret() {
        return secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public String getIssuer() {
        return issuer;
    }

    public Long getClockSkew() {
        return clockSkew;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}