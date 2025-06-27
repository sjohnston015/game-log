package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.dto.AddGameRequestDTO;
import com.johnstondev.gamelog.dto.GameDetailDTO;
import com.johnstondev.gamelog.dto.GameLogEntryResponseDTO;
import com.johnstondev.gamelog.model.GameLogEntry;
import com.johnstondev.gamelog.model.GameStatus;
import com.johnstondev.gamelog.model.User;
import com.johnstondev.gamelog.repository.GameLogEntryRepository;
import com.johnstondev.gamelog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameLogEntryServiceTests {

    @Mock
    private GameLogEntryRepository gameLogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RawgService rawgService;

    @InjectMocks
    private GameLogEntryService gameLogEntryService;

    @Test
    public void addGameToGameLog_ValidRequest_SuccessfullyAddsGame() {

        Long userId = 1L;
        Long rawgId = 12345L;

        // create a test user
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("user");
        mockUser.setEmail("user@email.com");

        // create mock game details from RAWG API
        GameDetailDTO gameDetails = new GameDetailDTO();
        gameDetails.setId(rawgId);
        gameDetails.setName("Zelda: Breath of the Wild");
        gameDetails.setBackgroundImage("https://media.rawg.io/media/games/zelda.jpg");
        gameDetails.setRating(4.8);

        // create the request DTO which is what the user sends
        AddGameRequestDTO request = new AddGameRequestDTO(rawgId, GameStatus.PLANNING, 9);

        // what should be saved to database
        GameLogEntry savedEntry = new GameLogEntry();
        savedEntry.setId(1L); // db would generate
        savedEntry.setUser(mockUser);
        savedEntry.setRawgId(rawgId);
        savedEntry.setGameTitle("Zelda: Breath of the Wild");
        savedEntry.setGameCoverImage("https://media.rawg.io/media/games/zelda.jpg");
        savedEntry.setStatus(GameStatus.PLANNING);
        savedEntry.setRating(9);
        savedEntry.setAddedAt(LocalDateTime.now());
        savedEntry.setUpdatedAt(LocalDateTime.now());

        // --- config. mock behavior ---

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(rawgService.getGameDetails(rawgId)).thenReturn(gameDetails);
        when(gameLogRepository.save(any(GameLogEntry.class))).thenReturn(savedEntry);

        GameLogEntryResponseDTO result = gameLogEntryService.addGameToGameLog(userId, request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRawgGameId()).isEqualTo(rawgId);
        assertThat(result.getGameTitle()).isEqualTo("Zelda: Breath of the Wild");
        assertThat(result.getGameCoverImage()).isEqualTo("https://media.rawg.io/media/games/zelda.jpg");
        assertThat(result.getStatus()).isEqualTo(GameStatus.PLANNING);
        assertThat(result.getRating()).isEqualTo(9);

        verify(userRepository, times(1)).findById(userId);
        verify(rawgService, times(1)).getGameDetails(rawgId);
        verify(gameLogRepository, times(1)).save(any(GameLogEntry.class));
    }

    @Test
    public void addGameToGameLog_UserNotFound_ThrowsException() {

        Long nonExistentUserId = 999L;
        AddGameRequestDTO request = new AddGameRequestDTO(12345L, GameStatus.PLANNING, 8);

        // config. mock to return empty (user not found)
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // assertThrows() verifies that the method throws the expected exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameLogEntryService.addGameToGameLog(nonExistentUserId, request);
        });

        // verify the exception message contains helpful information
        assertThat(exception.getMessage()).contains("User not found");

        verify(rawgService, never()).getGameDetails(any());
        verify(gameLogRepository, never()).save(any());
    }
}
