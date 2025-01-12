package com.example.demo.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.clock-skew}")
    private Long clockSkew;

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