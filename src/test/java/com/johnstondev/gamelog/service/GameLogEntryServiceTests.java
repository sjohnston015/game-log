package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.dto.AddGameRequestDTO;
import com.johnstondev.gamelog.dto.GameDetailDTO;
import com.johnstondev.gamelog.dto.GameLogEntryResponseDTO;
import com.johnstondev.gamelog.dto.UpdateGameRequestDTO;
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
import java.util.Arrays;
import java.util.List;
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
    public void addGameToLogSuccess() {

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
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(rawgService.getGameDetails(rawgId)).thenReturn(gameDetails);
        when(gameLogRepository.save(any(GameLogEntry.class))).thenReturn(savedEntry);

        GameLogEntryResponseDTO result = gameLogEntryService.addGameToLog(userId, request);

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
    public void addGameToLogUserNotFound() {

        Long fakeUserId = 999L;
        AddGameRequestDTO request = new AddGameRequestDTO(12345L, GameStatus.PLANNING, 8);

        // config. mock to return empty (user not found)
        when(userRepository.existsById(fakeUserId)).thenReturn(false); // I cannot believe this

        // assertThrows() verifies that the method throws the expected exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameLogEntryService.addGameToLog(fakeUserId, request);
        });

        // verify the exception message contains helpful information
        assertThat(exception.getMessage()).contains("User not found");
        verify(rawgService, never()).getGameDetails(any());
        verify(gameLogRepository, never()).save(any());
    }

    @Test
    public void getUserGameLog() {

        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("user");
        mockUser.setEmail("user@email.com");

        // create multiple game log entries for this user
        GameLogEntry entry1 = new GameLogEntry();
        entry1.setId(1L);
        entry1.setUser(mockUser);
        entry1.setRawgId(111L);
        entry1.setGameTitle("Zelda: Breath of the Wild");
        entry1.setGameCoverImage("https://media.rawg.io/media/games/zelda.jpg");
        entry1.setStatus(GameStatus.COMPLETED);
        entry1.setRating(10);
        entry1.setAddedAt(LocalDateTime.now().minusDays(5));
        entry1.setUpdatedAt(LocalDateTime.now().minusDays(1));

        GameLogEntry entry2 = new GameLogEntry();
        entry2.setId(2L);
        entry2.setUser(mockUser);
        entry2.setRawgId(222L);
        entry2.setGameTitle("Elden Ring");
        entry2.setGameCoverImage("https://media.rawg.io/media/games/elden.jpg");
        entry2.setStatus(GameStatus.PLAYING);
        entry2.setRating(null); // No rating yet
        entry2.setAddedAt(LocalDateTime.now().minusDays(2));
        entry2.setUpdatedAt(LocalDateTime.now());

        List<GameLogEntry> userGameEntries = Arrays.asList(entry1, entry2);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(gameLogRepository.findByUserId(userId)).thenReturn(userGameEntries);

        List<GameLogEntryResponseDTO> result = gameLogEntryService.getUserGameLog(userId);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        // check first game
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getRawgGameId()).isEqualTo(111L);
        assertThat(result.get(0).getGameTitle()).isEqualTo("Zelda: Breath of the Wild");
        assertThat(result.get(0).getStatus()).isEqualTo(GameStatus.COMPLETED);
        assertThat(result.get(0).getRating()).isEqualTo(10);

        // check second game
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getRawgGameId()).isEqualTo(222L);
        assertThat(result.get(1).getGameTitle()).isEqualTo("Elden Ring");
        assertThat(result.get(1).getStatus()).isEqualTo(GameStatus.PLAYING);
        assertThat(result.get(1).getRating()).isNull(); // No rating

        verify(userRepository, times(1)).existsById(userId);
        verify(gameLogRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void getUserGameLogUserNotFound() {

        Long nonExistentUserId = 999L;

        // config. mock to return false (user doesn't exist)
        when(userRepository.existsById(nonExistentUserId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameLogEntryService.getUserGameLog(nonExistentUserId);
        });

        // verify exception message
        assertThat(exception.getMessage()).contains("User not found with id: " + nonExistentUserId);

        // security check --- should NOT query for games if user doesn't exist
        verify(gameLogRepository, never()).findByUserId(any());
    }

    @Test
    public void getUserGameLogFilteredForCompleted() {

        Long userId = 1L;
        GameStatus targetStatus = GameStatus.COMPLETED;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("user");
        mockUser.setEmail("user@email.com");

        // create game entries with COMPLETED status (filtering)
        GameLogEntry completedGame1 = new GameLogEntry();
        completedGame1.setId(1L);
        completedGame1.setUser(mockUser);
        completedGame1.setRawgId(111L);
        completedGame1.setGameTitle("Zelda: Breath of the Wild");
        completedGame1.setGameCoverImage("https://media.rawg.io/media/games/zelda.jpg");
        completedGame1.setStatus(GameStatus.COMPLETED);
        completedGame1.setRating(10);
        completedGame1.setAddedAt(LocalDateTime.now().minusDays(10));
        completedGame1.setUpdatedAt(LocalDateTime.now().minusDays(5));

        GameLogEntry completedGame2 = new GameLogEntry();
        completedGame2.setId(2L);
        completedGame2.setUser(mockUser);
        completedGame2.setRawgId(222L);
        completedGame2.setGameTitle("Elden Ring");
        completedGame2.setGameCoverImage("https://media.rawg.io/media/games/elden.jpg");
        completedGame2.setStatus(GameStatus.COMPLETED);
        completedGame2.setRating(9);
        completedGame2.setAddedAt(LocalDateTime.now().minusDays(7));
        completedGame2.setUpdatedAt(LocalDateTime.now().minusDays(2));

        List<GameLogEntry> completedGames = Arrays.asList(completedGame1, completedGame2);

        // config. mocks
        when(userRepository.existsById(userId)).thenReturn(true);
        when(gameLogRepository.findByUserIdAndStatus(userId, targetStatus)).thenReturn(completedGames);

        List<GameLogEntryResponseDTO> result = gameLogEntryService.getUserGameLogByStatus(userId, targetStatus);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        // verify first game
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getRawgGameId()).isEqualTo(111L);
        assertThat(result.get(0).getGameTitle()).isEqualTo("Zelda: Breath of the Wild");
        assertThat(result.get(0).getStatus()).isEqualTo(GameStatus.COMPLETED);
        assertThat(result.get(0).getRating()).isEqualTo(10);

        // verify second game
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getRawgGameId()).isEqualTo(222L);
        assertThat(result.get(1).getGameTitle()).isEqualTo("Elden Ring");
        assertThat(result.get(1).getStatus()).isEqualTo(GameStatus.COMPLETED);
        assertThat(result.get(1).getRating()).isEqualTo(9);

        // verify interactions
        verify(userRepository, times(1)).existsById(userId);
        verify(gameLogRepository, times(1)).findByUserIdAndStatus(userId, targetStatus);
    }

    @Test
    public void getUserGameLogByStatusUserNotFound() {

        Long nonExistentUserId = 999L;
        GameStatus targetStatus = GameStatus.COMPLETED;

        // config. mock to return false (user DNE)
        when(userRepository.existsById(nonExistentUserId)).thenReturn(false);

        // expecting runtime exception to be thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameLogEntryService.getUserGameLogByStatus(nonExistentUserId, targetStatus);
        });

        // verify exception message contains helpful information
        assertThat(exception.getMessage()).contains("User not found with id: " + nonExistentUserId);

        // security verification - should NOT query for game entries if user DNE
        verify(userRepository, times(1)).existsById(nonExistentUserId);
        verify(gameLogRepository, never()).findByUserIdAndStatus(any(), any());
    }

    @Test
    public void getUserGameLogByStatusDatabaseException() {

        Long userId = 1L;
        GameStatus targetStatus = GameStatus.PLAYING;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(gameLogRepository.findByUserIdAndStatus(userId, targetStatus))
                .thenThrow(new RuntimeException("Database connection failed"));

        // should propagate the database exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameLogEntryService.getUserGameLogByStatus(userId, targetStatus);
        });

        assertThat(exception.getMessage()).contains("Database connection failed");
    }

    @Test
    public void updateGameLogEntrySuccess() {

        Long userId = 1L;
        Long entryId = 1L;

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testuser");

        // create existing entry with original values
        GameLogEntry existingEntry = new GameLogEntry();
        existingEntry.setId(entryId);
        existingEntry.setUser(mockUser);
        existingEntry.setRawgId(12345L);
        existingEntry.setGameTitle("Zelda: Breath of the Wild");
        existingEntry.setGameCoverImage("https://media.rawg.io/media/games/zelda.jpg");
        existingEntry.setStatus(GameStatus.PLAYING);  // with original status
        existingEntry.setRating(null);                // with no rating initially
        existingEntry.setAddedAt(LocalDateTime.now().minusDays(5));
        existingEntry.setUpdatedAt(LocalDateTime.now().minusDays(1));

        // create update request - changing status and adding rating
        UpdateGameRequestDTO request = new UpdateGameRequestDTO();
        request.setStatus(GameStatus.COMPLETED);
        request.setRating(9);

        // config. mocks
        when(gameLogRepository.findByIdAndUserId(entryId, userId))
                .thenReturn(Optional.of(existingEntry));
        when(gameLogRepository.save(existingEntry)).thenReturn(existingEntry);

        GameLogEntryResponseDTO result = gameLogEntryService.updateGameLogEntry(userId, entryId, request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(entryId);
        assertThat(result.getRawgGameId()).isEqualTo(12345L);               // should remain unchanged
        assertThat(result.getGameTitle()).isEqualTo("Zelda: Breath of the Wild"); // should remain unchanged
        assertThat(result.getGameCoverImage()).isEqualTo("https://media.rawg.io/media/games/zelda.jpg"); // should remain unchanged
        assertThat(result.getStatus()).isEqualTo(GameStatus.COMPLETED);     // should be updated
        assertThat(result.getRating()).isEqualTo(9);                       // should be updated

        // verify repository interactions
        verify(gameLogRepository, times(1)).findByIdAndUserId(entryId, userId);
        verify(gameLogRepository, times(1)).save(existingEntry);

        // verify the actual entity was modified correctly
        assertThat(existingEntry.getStatus()).isEqualTo(GameStatus.COMPLETED);
        assertThat(existingEntry.getRating()).isEqualTo(9);
    }

    @Test
    public void updateGameLogEntryNotFoundThrowsException() {

        Long userId = 1L;
        Long nonExistentEntryId = 999L;

        UpdateGameRequestDTO request = new UpdateGameRequestDTO();
        request.setStatus(GameStatus.COMPLETED);
        request.setRating(8);

        // config. mock to return empty Optional (entry not found)
        when(gameLogRepository.findByIdAndUserId(nonExistentEntryId, userId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameLogEntryService.updateGameLogEntry(userId, nonExistentEntryId, request);
        });

        // verify exception message contains helpful information
        assertThat(exception.getMessage())
                .contains("Game log entry not found with id: " + nonExistentEntryId + " for user: " + userId);

        // security verification - should NOT attempt to save if entry not found
        verify(gameLogRepository, times(1)).findByIdAndUserId(nonExistentEntryId, userId);
        verify(gameLogRepository, never()).save(any(GameLogEntry.class));
    }

    @Test
    public void removeGameFromGameLogSuccess() {

        Long userId = 1L;
        Long entryId = 1L;

        GameLogEntry mockEntry = new GameLogEntry();
        mockEntry.setId(entryId);
        mockEntry.setRawgId(12345L);
        mockEntry.setGameTitle("Zelda: Breath of the Wild");
        mockEntry.setStatus(GameStatus.COMPLETED);

        // config. mocks
        when(userRepository.existsById(userId)).thenReturn(true);
        when(gameLogRepository.findByIdAndUserId(entryId, userId))
                .thenReturn(Optional.of(mockEntry));
        doNothing().when(gameLogRepository).delete(mockEntry);

        gameLogEntryService.removeGameFromGameLog(userId, entryId);

        verify(userRepository, times(1)).existsById(userId);
        verify(gameLogRepository, times(1)).findByIdAndUserId(entryId, userId);
        verify(gameLogRepository, times(1)).delete(mockEntry);
    }

    @Test
    public void removeGameFromLibraryUserNotFound() {

        Long nonExistentUserId = 999L;
        Long entryId = 1L;

        when(userRepository.existsById(nonExistentUserId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameLogEntryService.removeGameFromGameLog(nonExistentUserId, entryId);
        });

        assertThat(exception.getMessage()).contains("User not found with id: " + nonExistentUserId);

        // should not attempt to find or delete entry if user DNE
        verify(gameLogRepository, never()).findByIdAndUserId(any(), any());
        verify(gameLogRepository, never()).delete(any());
    }

    @Test
    public void removeGameFromGameLogEntryNotFound() {

        Long userId = 1L;
        Long nonExistentEntryId = 999L;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(gameLogRepository.findByIdAndUserId(nonExistentEntryId, userId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameLogEntryService.removeGameFromGameLog(userId, nonExistentEntryId);
        });

        assertThat(exception.getMessage())
                .contains("Game log entry not found with id: " + nonExistentEntryId + " for user: " + userId);

        // should not attempt to delete if entry not found
        verify(gameLogRepository, never()).delete(any());
    }
}
