package com.example.demo.util;

import com.example.demo.config.JwtConfig;
import com.example.demo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility per la gestione dei token JWT, inclusa la generazione e la validazione.
 */
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private final JwtConfig jwtConfig;
    private final SecretKey key;

    /**
     * Costruttore con iniezione delle dipendenze.
     *
     * @param jwtConfig Configurazione JWT.
     */
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
    }

    public String generateToken(User user) {
        log.info("Generating token for user: {}", user.getUsername());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .claim("email", user.getEmail())
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Integer extractUserId(String token) {
        log.debug("Extracting userId from token.");
        return extractClaim(token, claims -> claims.get("userId", Integer.class));
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    public boolean validateTokenForUserId(String token, Integer userId) {
        final Integer extractedUserId = extractUserId(token);
        boolean isValid = extractedUserId.equals(userId) && !isTokenExpired(token);
        if (isValid) {
            log.info("Token is valid for userId: {}", userId);
        } else {
            log.warn("Token validation failed for userId: {}", userId);
        }
        return isValid;
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date(System.currentTimeMillis() - jwtConfig.getClockSkew() * 1000));
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Failed to extract claims from token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid token.");
        }
    }

    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}
