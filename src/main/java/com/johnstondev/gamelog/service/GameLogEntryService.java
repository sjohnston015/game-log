package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.model.GameLogEntry;
import com.johnstondev.gamelog.repository.GameLogEntryRepository;
import com.johnstondev.gamelog.repository.UserRepository;
import org.springframework.stereotype.Service;

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

}
