package com.example.demo.util;

import com.example.demo.config.JwtConfig;
import com.example.demo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility per la gestione dei token JWT, inclusa la generazione e la validazione.
 */
@Component
public class JwtUtil {

    private final SecretKey key;
    private final JwtConfig jwtConfig;

    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;

        // Decodifica la chiave segreta e valida la lunghezza
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("La chiave segreta deve essere lunga almeno 256 bit (32 byte).");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un token JWT per un dato utente.
     *
     * @param user L'utente per il quale generare il token.
     * @return Il token JWT generato.
     */
    // Genera un token con i dati dell'utente
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .claim("email", user.getEmail())
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(key) // Usa la chiave aggiornata
                .compact();
    }

    /**
     * Estrae il nome utente da un token JWT.
     *
     * @param token Il token JWT da cui estrarre il nome utente.
     * @return Il nome utente estratto dal token.
     */
    public String extractUsername(String token) {
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
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    /**
     * Verifica se un token JWT è scaduto.
     *
     * @param token Il token JWT da controllare.
     * @return true se il token è scaduto, false altrimenti.
     */
    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date(System.currentTimeMillis() - jwtConfig.getClockSkew() * 1000));
    }

    /**
     * Estrae tutti i claims da un token JWT.
     *
     * @param token Il token JWT da cui estrarre i claims.
     * @return I claims estratti dal token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // Metodo aggiornato per impostare la chiave
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
        return extractAllClaims(token).get(claimName, claimType);
    }
}
