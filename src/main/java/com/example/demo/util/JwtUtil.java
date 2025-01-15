package com.example.demo.util;

import com.example.demo.config.JwtConfig;
import com.example.demo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
    }

    /**
     * Genera un token JWT per un dato utente.
     *
     * @param user L'utente per il quale generare il token.
     * @return Il token JWT generato.
     */
    public String generateToken(User user) {
        log.info("Generating token for user: {}", user.getUsername());
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .claim("email", user.getEmail())
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(key)
                .compact();
        log.debug("Token generated: {}", token);
        return token;
    }

    /**
     * Estrae il nome utente da un token JWT.
     *
     * @param token Il token JWT da cui estrarre il nome utente.
     * @return Il nome utente estratto dal token.
     */
    public String extractUsername(String token) {
        log.debug("Extracting username from token: {}", token);
        return extractAllClaims(token).getSubject();
    }

    /**
     * Valida un token JWT rispetto a un nome utente.
     *
     * @param token Il token JWT da validare.
     * @param username Il nome utente da confrontare.
     * @return true se il token è valido, false altrimenti.
     */
    public boolean validateToken(String token, String username) {
        log.debug("Validating token for username: {}", username);
        String extractedUsername = extractUsername(token);
        boolean isValid = extractedUsername.equals(username) && !isTokenExpired(token);
        if (isValid) {
            log.info("Token is valid for username: {}", username);
        } else {
            log.warn("Token validation failed for username: {}", username);
        }
        return isValid;
    }

    /**
     * Verifica se un token JWT è scaduto.
     *
     * @param token Il token JWT da controllare.
     * @return true se il token è scaduto, false altrimenti.
     */
    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        boolean isExpired = expiration.before(new Date(System.currentTimeMillis() - jwtConfig.getClockSkew() * 1000));
        log.debug("Token expiration status: {}", isExpired);
        return isExpired;
    }

    /**
     * Estrae tutti i claims da un token JWT.
     *
     * @param token Il token JWT da cui estrarre i claims.
     * @return I claims estratti dal token.
     */
    private Claims extractAllClaims(String token) {
        log.debug("Extracting all claims from token: {}", token);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Estrae un claim specifico da un token JWT.
     *
     * @param token Il token JWT da cui estrarre il claim.
     * @param claimName Il nome del claim da estrarre.
     * @param claimType La classe del tipo del claim.
     * @param <T> Il tipo del claim.
     * @return Il valore del claim estratto.
     */
    public <T> T extractClaim(String token, String claimName, Class<T> claimType) {
        log.debug("Extracting claim: {} from token: {}", claimName, token);
        return extractAllClaims(token).get(claimName, claimType);
    }

    public String validateTokenAndGetUsername(String token) {
        try {
            // Parsing e validazione del token
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            // Controlla la data di scadenza
            if (claims.getExpiration().before(new Date())) {
                throw new RuntimeException("Token expired.");
            }

            // Restituisce il nome utente dal token
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            throw new RuntimeException("Invalid token.");
        }
    }


}
