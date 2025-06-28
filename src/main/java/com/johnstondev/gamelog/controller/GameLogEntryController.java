package com.johnstondev.gamelog.controller;

import com.johnstondev.gamelog.service.GameLogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}/games")
public class GameLogEntryController {

    private final GameLogEntryService gameLogEntryService;

    @Autowired
    public GameLogEntryController(GameLogEntryService gameLogEntryService) {
        this.gameLogEntryService = gameLogEntryService;
    }
}
