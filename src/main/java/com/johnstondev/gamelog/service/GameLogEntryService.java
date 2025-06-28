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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public GameLogEntryResponseDTO addGameToLog(Long userId, AddGameRequestDTO request) {

        // verify user exists in db and get make RAWG call to get game details (as DTO)
        verifyUserExists(userId);
        User user = userRepository.findById(userId).get();
        GameDetailDTO gameDetails = rawgService.getGameDetails(request.getRawgId());

        // make new GameLogEntry (GLE) with user and RAWG data; then save to db
        GameLogEntry entry = createNewEntry(user, request, gameDetails);
        GameLogEntry saved = gameLogRepository.save(entry); // b/c this generates the ID in the database

        // convert to and then return response DTO
        return convertToResponseDTO(saved);
    }

    public List<GameLogEntryResponseDTO> getUserGameLog(Long userId) {

        // verify user and get entries for them
        verifyUserExists(userId);
        List<GameLogEntry> entries = gameLogRepository.findByUserId(userId);
        List<GameLogEntryResponseDTO> userGameLog = new ArrayList<>();

        // convert each entry to response DTO and add to return list
        // after researching for a bit - deciding to implement this method w/o using streams
        for (GameLogEntry entry : entries) {
            userGameLog.add(convertToResponseDTO(entry));
        }

        return userGameLog;
    }

    public List<GameLogEntryResponseDTO> getUserGameLogByStatus(Long userId, GameStatus status) {

        // verify user is not a myth and filter data from repo
        verifyUserExists(userId);
        List<GameLogEntry> entries = gameLogRepository.findByUserIdAndStatus(userId, status);

        // convert each entry to response DTO and return
        List<GameLogEntryResponseDTO> filtered = new ArrayList<>();
        for (GameLogEntry entry : entries) {
            filtered.add(convertToResponseDTO(entry));
        }

        return filtered;
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

    private void verifyUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            // runtime exception thrown for now
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    // helps create a GLE without having to do it over and over
    private GameLogEntry createNewEntry(User user, AddGameRequestDTO request, GameDetailDTO gameDetails) {
        GameLogEntry entry = new GameLogEntry();
        entry.setUser(user);
        entry.setRawgId(request.getRawgId());
        entry.setGameTitle(gameDetails.getName());
        entry.setGameCoverImage(gameDetails.getBackgroundImage());
        entry.setStatus(request.getStatus());
        entry.setRating(request.getRating()); // could be null
        return entry;
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
