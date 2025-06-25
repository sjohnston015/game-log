package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.dto.GameSearchResultDTO;
import com.johnstondev.gamelog.dto.RawgSearchResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RawgServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RawgService rawgService;

    private static RawgSearchResponseDTO getRawgSearchResponseDTO() {
        GameSearchResultDTO game1 = new GameSearchResultDTO();
        game1.setId(1L);
        game1.setName("The Legend of Zelda");
        game1.setRating(4.5);

        GameSearchResultDTO game2 = new GameSearchResultDTO();
        game2.setId(2L);
        game2.setName("Zelda: Breath of the Wild");
        game2.setRating(4.8);

        List<GameSearchResultDTO> games = Arrays.asList(game1, game2);

        RawgSearchResponseDTO mockResponse = new RawgSearchResponseDTO();
        mockResponse.setResults(games);
        mockResponse.setCount(2);
        return mockResponse;
    }

    @Test
    public void searchGames() {

        RawgSearchResponseDTO mockResponse = getRawgSearchResponseDTO();

        // mock RestTemplate to return our fake response
        when(restTemplate.getForObject(any(String.class), eq(RawgSearchResponseDTO.class)))
                .thenReturn(mockResponse);

        List<GameSearchResultDTO> result = rawgService.searchGames("zelda");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("The Legend of Zelda");
        assertThat(result.get(1).getName()).isEqualTo("Zelda: Breath of the Wild");

        // verify RestTemplate was called once
        verify(restTemplate, times(1)).getForObject(any(String.class), eq(RawgSearchResponseDTO.class));
    }

    @Test
    public void searchGamesWhenEmpty() {

        RawgSearchResponseDTO emptyResponse = new RawgSearchResponseDTO();
        emptyResponse.setResults(List.of());
        emptyResponse.setCount(0);

        when(restTemplate.getForObject(any(String.class), eq(RawgSearchResponseDTO.class)))
                .thenReturn(emptyResponse);

        List<GameSearchResultDTO> result = rawgService.searchGames("nonexistentgame");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void throwExceptionWhenApiCallFails() {

        when(restTemplate.getForObject(any(String.class), eq(RawgSearchResponseDTO.class)))
                .thenThrow(new RuntimeException("Connection failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rawgService.searchGames("zelda");
        });

        assertThat(exception.getMessage()).contains("Failed to search games");
    }
}