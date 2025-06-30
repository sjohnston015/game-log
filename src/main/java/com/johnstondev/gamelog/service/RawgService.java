package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.dto.GameDetailDTO;
import com.johnstondev.gamelog.dto.GameSearchResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
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

    // --- fixing searchGames() function and adding helpers to improve search result quality ---

    // simple search - user types "zelda", gets games
    public List<GameSearchResultDTO> searchGames(String query) {
        // build URL with parameters
        // - search: the query
        // - page_size: 40
        // - ordering: "-relevance,-rating,-added"
        // - search_precise: true
        // - search_exact: false
        // Make API call with restTemplate.getForObject()
        // if response is not null and has results
        // call improveSearchResults(query, response.getResults())
        // else return empty list
        // wrap in try-catch and throw RuntimeException on error
        return null;
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

    private List<GameSearchResultDTO> improveSearchResults(String query, List<GameSearchResultDTO> rawResults) {
        // convert query to lowercase
        // use stream() to:
        // .filter() with isRelevantResult()
        // .sorted() with calculateRelevanceScore()
        // .limit(20)
        // .collect(Collectors.toList())
        return null;
    }

    /**
     * Filter out clearly irrelevant results
     */
    private boolean isRelevantResult(GameSearchResultDTO game, String queryLower) {
        // get game name and convert to lowercase
        // split queryLower by spaces to get individual words
        // check if game name contains at least one query word
        // use Arrays.stream(queryWords).anyMatch(word -> gameName.contains(word))
        // return false if rating is not null and < 2.0
        // return false if ratingsCount is not null and < 5
        // return true if all checks pass
        return false;
    }

    private double calculateRelevanceScore(GameSearchResultDTO game, String queryLower) {
        // get game name in lowercase
        // start with score = 0.0
        // add points for different match types:
        // exact match (gameName.equals): +1000 points
        // starts with query (gameName.startsWith): +500 points
        // contains as whole word: +300 points
        // contains anywhere: +100 points
        // add bonus points
        // rating bonus: rating * 10 (if not null)
        // popularity bonus: Math.min(ratingsCount / 10.0, 50.0) (if not null)
        // subtract points:
        // DLC penalty: -100 if containsDlcKeywords() returns true
        // return final score
        return 0.0;
    }

    private boolean containsDlcKeywords(String gameName) {
        String gameNameLower = gameName.toLowerCase();
        String[] dlcKeywords = {
                "dlc", "expansion", "season pass", "episode", "chapter",
                "pack", "bundle", "collection", "edition", "remaster",
                "definitive", "goty", "complete", "ultimate", "deluxe",
                "enhanced", "director's cut", "special edition", "gold",
                "premium", "collector's", "anniversary", "remastered"
        };

        return Arrays.stream(dlcKeywords)
                .anyMatch(gameNameLower::contains);
    }
}
