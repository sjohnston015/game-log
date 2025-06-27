package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.dto.AddGameRequestDTO;
import com.johnstondev.gamelog.dto.GameLogEntryResponseDTO;
import com.johnstondev.gamelog.dto.UpdateGameRequestDTO;
import com.johnstondev.gamelog.model.GameLogEntry;
import com.johnstondev.gamelog.model.GameStatus;
import com.johnstondev.gamelog.repository.GameLogEntryRepository;
import com.johnstondev.gamelog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameLogEntryService {

    private final GameLogEntryRepository gameLogRepository;
    private final UserRepository userRepository;
    private final RawgService rawgService;

    public GameLogEntryService(GameLogEntryRepository gameLogRepository,
                               UserRepository userRepository,
                               RawgService rawgService) {
        this.gameLogRepository = gameLogRepository;
        this.userRepository = userRepository;
        this.rawgService = rawgService;
    }

    // add a specific game to a specific user's library
    public GameLogEntryResponseDTO addGameToGameLog(Long userId, AddGameRequestDTO request) {

        // verify user exists
        // call RAWG API to get game details
        // create new GameLogEntry with user data and RAWG data
        // save to db
        // convert to response DTO and return
        return null;
    }

    public List<GameLogEntryResponseDTO> getUserLibrary(Long userId) {
        // verify user is real
        // get all entries for that user
        // convert each entry to DTO
        // return list of DTOs
        return null;
    }

    public List<GameLogEntryResponseDTO> getUserLibraryByStatus(Long userId, GameStatus status) {
        // verify user is NOT a myth...
        // filter entries from repository
        // convert to response DTOs
        // return list of DTOs
        return null;
    }

    public GameLogEntryResponseDTO updateGameLogEntry(Long userId, Long entryId, UpdateGameRequestDTO request) {
        // find entry by user and entry id - think about security though as well
        // update only status/rating fields (for now)
        // save updated entry
        // convert to response DTO and return
        return null;
    }

    public void removeGameFromLibrary(Long userId, Long entryId) {
        // find entry to delete by user id and entry id and then delete the game from user's gamelog
    }

    // ----- helper methods -----

    // check if user exists, throw exception if not
    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            // runtime exception thrown for now
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    // convert GameLogEntry entity to DTO
    private GameLogEntryResponseDTO convertToResponseDTO(GameLogEntry entry) {
        return new GameLogEntryResponseDTO(
                entry.getId(),
                entry.getRawgId(),
                entry.getGameTitle(),
                entry.getGameCoverImage(),
                entry.getStatus(),
                entry.getRating(),
                entry.getAddedAt(),
                entry.getUpdatedAt()
        );
    }
}
