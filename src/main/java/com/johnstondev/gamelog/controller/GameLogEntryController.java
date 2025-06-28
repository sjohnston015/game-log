package com.johnstondev.gamelog.controller;

import com.johnstondev.gamelog.dto.AddGameRequestDTO;
import com.johnstondev.gamelog.dto.GameLogEntryResponseDTO;
import com.johnstondev.gamelog.dto.UpdateGameRequestDTO;
import com.johnstondev.gamelog.model.GameStatus;
import com.johnstondev.gamelog.service.GameLogEntryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/games")
public class GameLogEntryController {

    private final GameLogEntryService gameLogEntryService;

    @Autowired
    public GameLogEntryController(GameLogEntryService gameLogEntryService) {
        this.gameLogEntryService = gameLogEntryService;
    }

    // POST /api/users/{userId}/games - add game to user's gamelog
    @PostMapping
    public ResponseEntity<GameLogEntryResponseDTO> addGameToGameLog(
            @PathVariable Long userId,
            @Valid @RequestBody AddGameRequestDTO request) {
        try {
            GameLogEntryResponseDTO response = gameLogEntryService.addGameToLog(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            System.err.println("Error adding game to gamelog: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/users/{userId}/games - get user's complete gamelog
    @GetMapping
    public ResponseEntity<List<GameLogEntryResponseDTO>> getUserGameLog(@PathVariable Long userId) {
        try {
            List<GameLogEntryResponseDTO> gameLog = gameLogEntryService.getUserGameLog(userId);
            return ResponseEntity.ok(gameLog);
        } catch (RuntimeException e) {
            System.err.println("Error fetching user game gamelog: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/users/{userId}/games?status=COMPLETED - filter games by status
    @GetMapping(params = "status")
    public ResponseEntity<List<GameLogEntryResponseDTO>> getUserGamesByStatus(
            @PathVariable Long userId,
            @RequestParam GameStatus status) {
        try {
            List<GameLogEntryResponseDTO> filteredGames =
                    gameLogEntryService.getUserGameLogByStatus(userId, status);
            return ResponseEntity.ok(filteredGames);
        } catch (RuntimeException e) {
            System.err.println("Error fetching games by status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT /api/users/{userId}/games/{entryId} - update game status/rating
    @PutMapping("/{entryId}")
    public ResponseEntity<GameLogEntryResponseDTO> updateGameEntry(
            @PathVariable Long userId,
            @PathVariable Long entryId,
            @Valid @RequestBody UpdateGameRequestDTO request) {
        try {
            GameLogEntryResponseDTO response =
                    gameLogEntryService.updateGameLogEntry(userId, entryId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Error updating game entry: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE /api/users/{userId}/games/{entryId} - remove game from gamelog
    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> removeGameFromGameLog(
            @PathVariable Long userId,
            @PathVariable Long entryId) {
        try {
            gameLogEntryService.removeGameFromGameLog(userId, entryId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.err.println("Error removing game from gamelog: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}