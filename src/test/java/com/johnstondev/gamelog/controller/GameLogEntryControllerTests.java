package com.johnstondev.gamelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnstondev.gamelog.dto.AddGameRequestDTO;
import com.johnstondev.gamelog.dto.GameLogEntryResponseDTO;
import com.johnstondev.gamelog.dto.UpdateGameRequestDTO;
import com.johnstondev.gamelog.model.GameStatus;
import com.johnstondev.gamelog.service.GameLogEntryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = GameLogEntryController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class GameLogEntryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GameLogEntryService gameLogEntryService;

    @Test
    public void addGameToGameLogSuccess() throws Exception {

        Long userId = 1L;
        AddGameRequestDTO request = new AddGameRequestDTO(12345L, GameStatus.PLANNING, 8);

        GameLogEntryResponseDTO mockResponse = new GameLogEntryResponseDTO(
                1L, 12345L, "Zelda: Breath of the Wild",
                "https://media.rawg.io/media/games/zelda.jpg",
                GameStatus.PLANNING, 8, LocalDateTime.now(), LocalDateTime.now()
        );

        when(gameLogEntryService.addGameToLog(userId, request)).thenReturn(mockResponse);

        String requestBody = """
            {
                "rawgId": 12345,
                "status": "PLANNING",
                "rating": 8
            }
            """;

        mockMvc.perform(post("/api/users/{userId}/games", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rawgGameId").value(12345))
                .andExpect(jsonPath("$.gameTitle").value("Zelda: Breath of the Wild"))
                .andExpect(jsonPath("$.status").value("PLANNING"))
                .andExpect(jsonPath("$.rating").value(8));

        verify(gameLogEntryService, times(1)).addGameToLog(eq(userId), any(AddGameRequestDTO.class));
    }

    @Test
    public void addGameToGameLogValidationError() throws Exception {
        Long userId = 1L;

        // invalid request - missing required fields
        String invalidRequestBody = """
            {
                "rawgId": null,
                "status": null,
                "rating": 15
            }
            """;

        mockMvc.perform(post("/api/users/{userId}/games", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest());

        // service should never be called with invalid data
        verify(gameLogEntryService, never()).addGameToLog(any(), any());
    }

    @Test
    public void getUserGameGameLogSuccess() throws Exception {
        Long userId = 1L;

        List<GameLogEntryResponseDTO> mockGameLog = Arrays.asList(
                new GameLogEntryResponseDTO(1L, 12345L, "Zelda: Breath of the Wild",
                        "https://media.rawg.io/media/games/zelda.jpg", GameStatus.COMPLETED, 10,
                        LocalDateTime.now(), LocalDateTime.now()),
                new GameLogEntryResponseDTO(2L, 67890L, "Elden Ring",
                        "https://media.rawg.io/media/games/elden.jpg", GameStatus.PLAYING, null,
                        LocalDateTime.now(), LocalDateTime.now())
        );

        when(gameLogEntryService.getUserGameLog(userId)).thenReturn(mockGameLog);

        mockMvc.perform(get("/api/users/{userId}/games", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].gameTitle").value("Zelda: Breath of the Wild"))
                .andExpect(jsonPath("$[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$[1].gameTitle").value("Elden Ring"))
                .andExpect(jsonPath("$[1].status").value("PLAYING"));

        verify(gameLogEntryService, times(1)).getUserGameLog(userId);
    }

    @Test
    public void getUserGamesByStatusSuccess() throws Exception {
        Long userId = 1L;
        GameStatus status = GameStatus.COMPLETED;

        List<GameLogEntryResponseDTO> completedGames = Arrays.asList(
                new GameLogEntryResponseDTO(1L, 12345L, "Zelda: Breath of the Wild",
                        "https://media.rawg.io/media/games/zelda.jpg", GameStatus.COMPLETED, 10,
                        LocalDateTime.now(), LocalDateTime.now())
        );

        when(gameLogEntryService.getUserGameLogByStatus(userId, status)).thenReturn(completedGames);

        mockMvc.perform(get("/api/users/{userId}/games", userId)
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("COMPLETED"));

        verify(gameLogEntryService, times(1)).getUserGameLogByStatus(userId, status);
    }

    @Test
    public void updateGameEntrySuccess() throws Exception {
        Long userId = 1L;
        Long entryId = 1L;

        GameLogEntryResponseDTO mockResponse = new GameLogEntryResponseDTO(
                entryId, 12345L, "Zelda: Breath of the Wild",
                "https://media.rawg.io/media/games/zelda.jpg",
                GameStatus.COMPLETED, 9, LocalDateTime.now(), LocalDateTime.now()
        );

        when(gameLogEntryService.updateGameLogEntry(eq(userId), eq(entryId), any(UpdateGameRequestDTO.class)))
                .thenReturn(mockResponse);

        String requestBody = """
            {
                "status": "COMPLETED",
                "rating": 9
            }
            """;

        mockMvc.perform(put("/api/users/{userId}/games/{entryId}", userId, entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.rating").value(9));

        verify(gameLogEntryService, times(1)).updateGameLogEntry(eq(userId), eq(entryId), any(UpdateGameRequestDTO.class));
    }

    @Test
    public void removeGameFromGameLogSuccess() throws Exception {
        Long userId = 1L;
        Long entryId = 1L;

        doNothing().when(gameLogEntryService).removeGameFromGameLog(userId, entryId);

        mockMvc.perform(delete("/api/users/{userId}/games/{entryId}", userId, entryId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(gameLogEntryService, times(1)).removeGameFromGameLog(userId, entryId);
    }

    @Test
    public void handleServiceExceptionNicely() throws Exception {
        Long userId = 999L; // non-existent user

        when(gameLogEntryService.getUserGameLog(userId))
                .thenThrow(new RuntimeException("User not found with id: 999"));

        mockMvc.perform(get("/api/users/{userId}/games", userId))
                .andExpect(status().isInternalServerError());

        verify(gameLogEntryService, times(1)).getUserGameLog(userId);
    }
}