package com.example.demo.security;

import com.example.demo.util.JwtUtil;
import com.example.demo.util.MessageHeaderHolder;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Filtro di autenticazione per validare i token JWT nelle richieste in ingresso.
 * Questo filtro viene eseguito una volta per ogni richiesta e verifica la validità del token JWT.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final MessageHeaderHolder messageHeaderHolder;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, MessageHeaderHolder messageHeaderHolder) {
        this.jwtUtil = jwtUtil;
        this.messageHeaderHolder = messageHeaderHolder;
    }

    /**
     * Determina se il filtro deve essere applicato alla richiesta corrente.
     *
     * @param request La richiesta HTTP in ingresso.
     * @return true se il filtro non deve essere applicato, false altrimenti.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldNotFilter = path.equals("/api/user/register") || path.equals("/api/auth/login");
        log.debug("shouldNotFilter: {} for path: {}", shouldNotFilter, path);
        return shouldNotFilter;
    }

    /**
     * Esegue il filtro per validare il token JWT presente nell'intestazione Authorization.
     *
     * @param request La richiesta HTTP in ingresso.
     * @param response La risposta HTTP in uscita.
     * @param filterChain La catena di filtri.
     * @throws ServletException In caso di errori di servlet.
     * @throws IOException In caso di errori di I/O.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            log.debug("Authorization header: {}", authorizationHeader);

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                log.debug("JWT token extracted: {}", token);

                String username = jwtUtil.extractUsername(token);
                log.debug("Username extracted from token: {}", username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.validateToken(token, username)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(username, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        log.info("Authentication set for user: {}", username);
                    } else {
                        log.warn("Invalid token for username: {}", username);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            log.error("JWT exception: {}", ex.getMessage());
            // Gestisce errori legati al token JWT
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(String.format(
                    "{\"error\": \"%s\"}",
                    messageHeaderHolder.getMessage("auth.invalid")
            ));
        }
    }
}