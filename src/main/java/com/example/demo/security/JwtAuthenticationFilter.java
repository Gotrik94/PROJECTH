package com.example.demo.security;

import com.example.demo.util.JwtUtil;
import com.example.demo.util.MessageHeaderHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MessageHeaderHolder messageHeaderHolder;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, MessageHeaderHolder messageHeaderHolder) {
        this.jwtUtil = jwtUtil;
        this.messageHeaderHolder = messageHeaderHolder;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Escludi i path non protetti
        String path = request.getRequestURI();
        return path.equals("/api/user/register"); // Aggiungi altri path esclusi se necessario
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                String username = jwtUtil.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.validateToken(token, username)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(username, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // In caso di errore, restituisci un messaggio dettagliato
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(String.format(
                    "{\"error\": \"%s\"}",
                    messageHeaderHolder.getMessage("auth.invalid")
            ));
        }
    }
}
