package com.johnstondev.gamelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnstondev.gamelog.model.User;
import com.johnstondev.gamelog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
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

        when(userService.createUser("user", "user@email.com", "password1234"))
                .thenReturn(user);

        // create JSON request
        String requestBody = """
                {
                    "username": "user",
                    "email": "user@email.com",
                    "password": "password1234"
                }
                """;

        // make the HTTP request and verify the response
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user"))
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
    public void should404WhenUserNotFound() throws Exception {

        when(userService.findUserById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should400WhenUsernameIsBlank() throws Exception {
        String requestBody = """
            {
                "username": "",
                "email": "test@email.com",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should400WhenEmailIsInvalid() throws Exception {
        String requestBody = """
            {
                "username": "user",
                "email": "invalid-email",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should400WhenPasswordIsTooShort() throws Exception {
        String requestBody = """
            {
                "username": "user",
                "email": "user@email.com",
                "password": "short"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUser() throws Exception {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("updatedusername");
        user.setEmail("updated@email.com");
        user.setPasswordHash("originalpassword_hashed");
        user.setCreatedAt(LocalDateTime.now().minusDays(1));

        when(userService.updateUser(id, "updatedusername", "updated@email.com"))
                .thenReturn(user);

        String requestBody = """
                {
                    "username": "updatedusername",
                    "email": "updated@email.com"
                }
                """;

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.username").value("updatedusername"))
                .andExpect(jsonPath("$.email").value("updated@email.com"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist())
                .andExpect(jsonPath("$.createdAt").exists());

        verify(userService, times(1)).updateUser(id, "updatedusername", "updated@email.com");
    }

    @Test
    public void updateIndividualUserField() throws Exception {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("newusername");
        user.setEmail("old@email.com");
        user.setCreatedAt(LocalDateTime.now().minusDays(1));

        when(userService.updateUser(id, "newusername", null))
                .thenReturn(user);

        String requestBody = """
                {
                    "username": "newusername"
                }
                """;

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newusername"))
                .andExpect(jsonPath("$.email").value("old@email.com"));

        verify(userService, times(1)).updateUser(id, "newusername", null);
    }

    @Test
    public void return400WhenUpdateDataIsInvalid() throws Exception {
        String requestBody = """
                {
                    "username": "ab",
                    "email": "valid@email.com"
                }
                """;

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(any(), any(), any());
    }

    @Test
    public void return400WhenEmailIsInvalidInUpdate() throws Exception {
        String requestBody = """
                {
                    "username": "validusername",
                    "email": "not-an-email"
                }
                """;

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(any(), any(), any());
    }

    @Test
    public void handleServiceExceptionDuringUpdate() throws Exception {
        Long id = 999L;
        when(userService.updateUser(id, "username", "email@test.com"))
                .thenThrow(new RuntimeException("User no found with id: 999"));

        String requestBody = """
                {
                    "username": "username",
                    "email": "email@test.com"
                }
                """;

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError());

        verify(userService, times(1)).updateUser(id, "username", "email@test.com");
    }

    @Test
    public void deleteUser() throws Exception {
        Long id = 1L;
        doNothing().when(userService).deleteUser(id);

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(userService, times(1)).deleteUser(id);
    }

    @Test
    public void return500WhenDeletingNonExistentUser() throws Exception {
        Long id = 999L;
        doThrow(new RuntimeException("User does not exist with id: 999"))
                .when(userService).deleteUser(id);

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isInternalServerError());

        verify(userService, times(1)).deleteUser(id);
    }

    @Test
    public void handleDatabaseErrorDuringDelete() throws Exception {
        Long id = 1L;
        doThrow(new RuntimeException("Database connection failed"))
                .when(userService).deleteUser(id);

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isInternalServerError());

        verify(userService, times(1)).deleteUser(id);
    }

    @Test
    public void acceptValidPathVariableForDelete() throws Exception {
        Long[] ids = {1L, 42L, 999L, 123456L};

        for (Long id : ids) {
            doNothing().when(userService).deleteUser(id);

            mockMvc.perform(delete("/api/users/{id}", id))
                    .andExpect(status().isNoContent());

            verify(userService, times(1)).deleteUser(id);
            reset(userService);
        }
    }
}
