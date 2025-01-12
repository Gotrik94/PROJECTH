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
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private SecretKey key;

    @BeforeEach
    public void setup() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
        Locale.setDefault(Locale.ENGLISH); // Imposta il locale di default per i test
    }

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

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "en")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User successfully created!"))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("testuser@example.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = new User();
        user.setUsername("deleteuser");
        user.setPassword("password123");
        user.setEmail("deleteuser@example.com");
        userService.registerUser(user);

        String token = generateToken("deleteuser");

        mockMvc.perform(delete("/api/user/delete")
                        .header("Authorization", "Bearer " + token)
                        .header("Accept-Language", "en")
                        .param("username", "deleteuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User successfully deleted."))
                .andExpect(jsonPath("$.code").value(200));

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

        mockMvc.perform(put("/api/user/update-status")
                        .header("Authorization", "Bearer " + token)
                        .header("Accept-Language", "en")
                        .param("username", "statususer")
                        .param("status", "ONLINE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Status successfully updated."))
                .andExpect(jsonPath("$.code").value(200));

        User updatedUser = userService.findByUsername("statususer").orElseThrow();
        assert updatedUser.getStatus() == UserStatus.ONLINE;
    }

    @Test
    public void testDeleteNonExistentUser() throws Exception {
        String token = generateToken("nonexistentuser");

        mockMvc.perform(delete("/api/user/delete")
                        .header("Authorization", "Bearer " + token)
                        .header("Accept-Language", "en")
                        .param("username", "nonexistentuser"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found."))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    public void testGetUserProfile() throws Exception {
        User user = new User();
        user.setUsername("profileuser");
        user.setPassword("password123");
        user.setEmail("profile@example.com");
        userService.registerUser(user);

        String token = generateToken("profileuser");

        mockMvc.perform(get("/api/user/profile")
                        .header("Authorization", "Bearer " + token)
                        .header("Accept-Language", "en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User profile found."))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("profileuser"))
                .andExpect(jsonPath("$.data.email").value("profile@example.com"));
    }

    @Test
    public void testUpdateGameStats() throws Exception {
        User user = new User();
        user.setUsername("statsuser");
        user.setPassword("password123");
        user.setEmail("stats@example.com");
        userService.registerUser(user);

        String token = generateToken("statsuser");

        mockMvc.perform(put("/api/user/update-stats")
                        .header("Authorization", "Bearer " + token)
                        .header("Accept-Language", "en")
                        .param("username", "statsuser")
                        .param("gamesPlayed", "10")
                        .param("gamesWon", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Game statistics successfully updated."))
                .andExpect(jsonPath("$.code").value(200));

        User updatedUser = userService.findByUsername("statsuser").orElseThrow();
        assert updatedUser.getGamesPlayed() == 10;
        assert updatedUser.getGamesWon() == 5;
    }

    @Test
    public void testDuplicateUsername() throws Exception {
        User user = new User();
        user.setUsername("duplicateuser");
        user.setPassword("password123");
        user.setEmail("unique@example.com");
        userService.registerUser(user);

        User duplicateUser = new User();
        duplicateUser.setUsername("duplicateuser");
        duplicateUser.setPassword("password456");
        duplicateUser.setEmail("different@example.com");

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "en")
                        .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The user provided is already in use."))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void testDuplicateEmail() throws Exception {
        User user = new User();
        user.setUsername("uniqueuser");
        user.setPassword("password123");
        user.setEmail("duplicate@example.com");
        userService.registerUser(user);

        User duplicateEmailUser = new User();
        duplicateEmailUser.setUsername("differentuser");
        duplicateEmailUser.setPassword("password456");
        duplicateEmailUser.setEmail("duplicate@example.com");

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "en")
                        .content(objectMapper.writeValueAsString(duplicateEmailUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The email provided is already in use."))
                .andExpect(jsonPath("$.code").value(400));
    }
}
