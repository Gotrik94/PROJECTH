package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(String username, String password) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username o password errati"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Username o password errati");
        }

        // Genera e restituisce il token JWT
        return jwtUtil.generateToken(user);
    }
}
