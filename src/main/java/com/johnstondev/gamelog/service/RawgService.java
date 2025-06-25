package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.dto.GameDetailDTO;
import com.johnstondev.gamelog.dto.GameSearchResultDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RawgService {

    private final String apiKey;
    private final String baseUrl;

    // constructor injection instead of field injection
    public RawgService(@Value("${rawg.api.key}") String apiKey,
                       @Value("${rawg.api.base-url}") String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    // Simple search - user types "zelda", gets games
    public List<GameSearchResultDTO> searchGames(String query) {
        return null;
    }

    // Advanced search - with pagination
    public List<GameSearchResultDTO> searchGames(String query, int page, int pageSize) {
        return null;
    }

    // Get detailed info about one specific game
    public GameDetailDTO getGameDetails(Long rawgGameId) {
        return null;
    }

}
