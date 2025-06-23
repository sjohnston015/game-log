package com.johnstondev.gamelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnstondev.gamelog.model.User;
import com.johnstondev.gamelog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void createUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@email.com");
        user.setPasswordHash("password123_hashed");

        when(userService.createUser("user", "user@email.com", "password123"))
                .thenReturn(user);

        // create JSON request
        String requestBody = """
                {
                    "username": "user",
                    "email": "user@email.com",
                    "password": "password123"
                }
                """;

        // make the HTTP request and verify the response
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("useruser"))
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist()); // shouldn't expose password hash
    }

    @Test
    public void shouldGetUserById() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@email.com");
        user.setPasswordHash("secret_hashed");

        when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist()); // shouldn't expose password hash
    }

    @Test
    public void shouldReturn404WhenUserNotFound() throws Exception {

        when(userService.findUserById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }
}
