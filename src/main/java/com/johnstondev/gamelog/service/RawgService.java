package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.dto.GameDetailDTO;
import com.johnstondev.gamelog.dto.GameSearchResultDTO;
import com.johnstondev.gamelog.dto.RawgSearchResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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

    // --- fixing searchGames() function and adding helpers to improve search result quality ---

    // simple search - user types "zelda", gets games
    public List<GameSearchResultDTO> searchGames(String query) {

        // build URL with parameters
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/games")
                .queryParam("key", apiKey)
                .queryParam("search", query)
                .queryParam("page_size", 20) // fewer results? more high quality?
                .queryParam("ordering", "-added,-rating")
                .queryParam("search_precise", false)
                .queryParam("search_exact", false)
                .toUriString();

        try {

            // make API call with restTemplate.getForObject()
            RawgSearchResponseDTO response = restTemplate.getForObject(url, RawgSearchResponseDTO.class);

            if (response != null && response.getResults() != null) {
                return response.getResults();
            } else {
                return List.of();
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to search: " + e.getMessage(), e);
        }
    }

    // advanced search - with pagination
    public List<GameSearchResultDTO> searchGames(String query, int page, int pageSize) {
        return null;
    }

    // get detailed info about one specific game
    public GameDetailDTO getGameDetails(Long rawgId) {

        // build URL for specific game endpoint
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/games/" + rawgId)
                .queryParam("key", apiKey)
                .toUriString();

        try {
            // get raw response for debugging
            String rawResponse = restTemplate.getForObject(url, String.class);
            System.out.println("Raw game details response: " + (rawResponse != null ?
                    rawResponse.substring(0, Math.min(200, rawResponse.length())) + "..." : "null"));

            // parse to DTO
            GameDetailDTO gameDetails = restTemplate.getForObject(url, GameDetailDTO.class);

            // validate response
            if (gameDetails != null && gameDetails.getName() != null) {
                System.out.println("Successfully fetched game: " + gameDetails.getName());
                return gameDetails;
            } else {
                throw new RuntimeException("Game not found or invalid response for RAWG ID: " + rawgId);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
