package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.dto.GameDetailDTO;
import com.johnstondev.gamelog.dto.GameSearchResultDTO;
import com.johnstondev.gamelog.dto.RawgSearchResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class RawgService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    // constructor injection instead of field injection
    public RawgService(RestTemplate restTemplate,
                       @Value("${rawg.api.key}") String apiKey,
                       @Value("${rawg.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    // simple search - user types "zelda", gets games
    public List<GameSearchResultDTO> searchGames(String query) {

        // build the url -> http call -> extract and return
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/games")
                .queryParam("key", apiKey)
                .queryParam("search", query)
                .queryParam("page_size", 20)
                .queryParam("ordering", "-rating")
                .toUriString();

        try {
            RawgSearchResponseDTO response = restTemplate.getForObject(url, RawgSearchResponseDTO.class);

            if (response != null && response.getResults() != null) {
                return response.getResults();
            } else {
                return List.of(); // Return empty list if no results
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to search games: " + e.getMessage(), e);
        }

    }

    // advanced search - with pagination
    public List<GameSearchResultDTO> searchGames(String query, int page, int pageSize) {
        return null;
    }

    // get detailed info about one specific game
    public GameDetailDTO getGameDetails(Long rawgGameId) {
        return null;
    }

}
