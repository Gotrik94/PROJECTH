package com.example.demo;

import com.example.demo.enums.UserStatus;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtConfig jwtConfig;

    @BeforeEach
    public void setup() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
    }

    private SecretKey key;

    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", "USER")
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(key)
                .compact();
    }

    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = new User();
        user.setUsername("deleteuser");
        user.setPassword("password123");
        user.setEmail("deleteuser@example.com");
        userService.registerUser(user);

        String token = generateToken("deleteuser");

        mockMvc.perform(delete("/api/users/delete")
                        .header("Authorization", "Bearer " + token)
                        .param("username", "deleteuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("Utente eliminato con successo"));

        assert userService.findByUsername("deleteuser").isEmpty();
    }

    @Test
    public void testUpdateUserStatus() throws Exception {
        User user = new User();
        user.setUsername("statususer");
        user.setPassword("password123");
        user.setEmail("statususer@example.com");
        userService.registerUser(user);

        String token = generateToken("statususer");

        mockMvc.perform(put("/api/users/update-status")
                        .header("Authorization", "Bearer " + token)
                        .param("username", "statususer")
                        .param("status", "ONLINE"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stato aggiornato con successo"));

        User updatedUser = userService.findByUsername("statususer").orElseThrow();
        assert updatedUser.getStatus() == UserStatus.ONLINE;
    }

    @Test
    public void testDeleteNonExistentUser() throws Exception {
        String token = generateToken("nonexistentuser");

        mockMvc.perform(delete("/api/users/delete")
                        .header("Authorization", "Bearer " + token)
                        .param("username", "nonexistentuser"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Utente non trovato"));
    }

    @Test
    public void testGetUserProfile() throws Exception {
        User user = new User();
        user.setUsername("profileuser");
        user.setPassword("password123");
        user.setEmail("profile@example.com");
        userService.registerUser(user);

        String token = generateToken("profileuser");

        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("profileuser"))
                .andExpect(jsonPath("$.email").value("profile@example.com"));
    }

    @Test
    public void testUpdateGameStats() throws Exception {
        User user = new User();
        user.setUsername("statsuser");
        user.setPassword("password123");
        user.setEmail("stats@example.com");
        userService.registerUser(user);

        String token = generateToken("statsuser");

        mockMvc.perform(put("/api/users/update-stats")
                        .header("Authorization", "Bearer " + token)
                        .param("username", "statsuser")
                        .param("gamesPlayed", "10")
                        .param("gamesWon", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Statistiche aggiornate con successo"));

        User updatedUser = userService.findByUsername("statsuser").orElseThrow();
        assert updatedUser.getGamesPlayed() == 10;
        assert updatedUser.getGamesWon() == 5;
    }
}
