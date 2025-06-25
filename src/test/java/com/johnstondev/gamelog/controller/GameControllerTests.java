package com.johnstondev.gamelog.controller;

import com.johnstondev.gamelog.dto.GameSearchResultDTO;
import com.johnstondev.gamelog.service.RawgService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = GameController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class GameControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RawgService rawgService;

    @Test
    public void searchGames() throws Exception {
        // create mock games
        GameSearchResultDTO game1 = new GameSearchResultDTO();
        game1.setId(1L);
        game1.setName("The Legend of Zelda");
        game1.setRating(4.5);

        GameSearchResultDTO game2 = new GameSearchResultDTO();
        game2.setId(2L);
        game2.setName("Zelda: Breath of the Wild");
        game2.setRating(4.8);

        List<GameSearchResultDTO> games = Arrays.asList(game1, game2);

        when(rawgService.searchGames("zelda")).thenReturn(games);

        mockMvc.perform(get("/api/games/search").param("q", "zelda"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("The Legend of Zelda"))
                .andExpect(jsonPath("$[0].rating").value(4.5))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Zelda: Breath of the Wild"));

        verify(rawgService, times(1)).searchGames("zelda");
    }

    @Test
    public void searchGamesWhenEmpty() throws Exception {
        // empty results
        when(rawgService.searchGames("nonexistent")).thenReturn(List.of());

        mockMvc.perform(get("/api/games/search").param("q", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(rawgService, times(1)).searchGames("nonexistent");
    }

    @Test
    public void return500WhenServiceThrowsException() throws Exception {
        // service should throw an exception
        when(rawgService.searchGames("error")).thenThrow(new RuntimeException("API failed"));

        mockMvc.perform(get("/api/games/search").param("q", "error"))
                .andExpect(status().isInternalServerError());

        verify(rawgService, times(1)).searchGames("error");
    }
}