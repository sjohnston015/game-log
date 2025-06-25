package com.johnstondev.gamelog.controller;

import com.johnstondev.gamelog.dto.GameSearchResultDTO;
import com.johnstondev.gamelog.service.RawgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final RawgService rawgService;

    @Autowired
    public GameController(RawgService rawgService) {
        this.rawgService = rawgService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Controller is working!");
    }

    @GetMapping("/search")
    public ResponseEntity<List<GameSearchResultDTO>> searchGames(@RequestParam String q) {
        try {
            List<GameSearchResultDTO> games = rawgService.searchGames(q);
            return ResponseEntity.ok(games);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
