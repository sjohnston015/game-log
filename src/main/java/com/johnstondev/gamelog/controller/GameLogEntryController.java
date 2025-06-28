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

    @PostMapping
    public ResponseEntity<GameLogEntryResponseDTO> addGameToGameLog(
            @PathVariable Long userId,
            @Valid @RequestBody AddGameRequestDTO request) {

        try {
            GameLogEntryResponseDTO response = gameLogEntryService.addGameToLog(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<GameLogEntryResponseDTO>> getUserGameLog(
            @PathVariable Long userId,
            @RequestParam(required = false) GameStatus status) {

        try {
            List<GameLogEntryResponseDTO> gameLog;

            if (status != null) {
                // filter by status if provided
                gameLog = gameLogEntryService.getUserGameLogByStatus(userId, status);
            } else {
                // get all games for user
                gameLog = gameLogEntryService.getUserGameLog(userId);
            }

            return ResponseEntity.ok(gameLog);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{entryId}")
    public ResponseEntity<GameLogEntryResponseDTO> updateGameEntry(
            @PathVariable Long userId,
            @PathVariable Long entryId,
            @Valid @RequestBody UpdateGameRequestDTO request) {

        try {
            GameLogEntryResponseDTO response = gameLogEntryService.updateGameLogEntry(userId, entryId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> removeGameFromGameLog(
            @PathVariable Long userId,
            @PathVariable Long entryId) {

        try {
            gameLogEntryService.removeGameFromGameLog(userId, entryId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}