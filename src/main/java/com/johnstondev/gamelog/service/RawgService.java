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
                .queryParam("page_size", 40)
                .queryParam("ordering", "-relevance,-rating,-added")
                .queryParam("search_precise", true)
                .queryParam("search_exact", false)
                .toUriString();

        try {

            // make API call with restTemplate.getForObject()
            RawgSearchResponseDTO response = restTemplate.getForObject(url, RawgSearchResponseDTO.class);

            if (response != null && response.getResults() != null) {
                return improveSearchResults(query, response.getResults());
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

    private List<GameSearchResultDTO> improveSearchResults(String query, List<GameSearchResultDTO> rawResults) {
        String queryLower = query.toLowerCase().trim();

        return rawResults.stream()
                .filter(game -> isRelevantResult(game, queryLower))
                .sorted((game1, game2) -> {
                        double score1 = calculateRelevanceScore(game1, queryLower);
                        double score2 = calculateRelevanceScore(game2, queryLower);
                        return Double.compare(score1, score2);
                })
                .limit(20)
                .toList();
    }

    private boolean isRelevantResult(GameSearchResultDTO game, String queryLower) {

        String gameName = game.getName().toLowerCase();
        String[] queryWords = queryLower.split("\\s+");

        // for multi-word searches, require better matching
        if (queryWords.length > 1) {
            boolean isRelevant = false;
            // -- game must contain the full phrase
            if (gameName.contains(queryLower)) {
                isRelevant = true;
            }
            // -- game must contain ALL words from the query
            else if (Arrays.stream(queryWords).allMatch(gameName::contains)) {
                isRelevant = true;
            }
            // -- game contains most words and has high relevance
            else {
                long matchingWords = Arrays.stream(queryWords)
                        .mapToLong(word -> gameName.contains(word) ? 1 : 0)
                        .sum();

                // require at least 50% of words to match for multi-word queries
                if (matchingWords >= Math.ceil(queryWords.length * 0.5)) {
                    isRelevant = true;
                }
            }

            // if none of the above conditions were met, this game is not relevant
            if (!isRelevant) {
                return false;
            }

        } else {
            // for single word queries, use the original logic
            if (!gameName.contains(queryWords[0])) {
                return false;
            }
        }

        // filter out games with very low ratings or few reviews
        if (game.getRating() != null && game.getRating() < 2.0) {
            return false;
        }

        return game.getRatingsCount() == null || game.getRatingsCount() >= 5;
    }

    private double calculateRelevanceScore(GameSearchResultDTO game, String queryLower) {

        String gameName = game.getName().toLowerCase();
        double score = 0.0;

        // exact match gets highest score
        if (gameName.equals(queryLower)) {
            score += 1000.0;
        }

        // title starts with query gets high score
        if (gameName.startsWith(queryLower)) {
            score += 500.0;
        }

        // title contains query as whole word
        if (gameName.contains(" " + queryLower + " ") ||
                gameName.startsWith(queryLower + " ") ||
                gameName.endsWith(" " + queryLower)) {
            score += 300.0;
        }

        // partial match gets some points
        if (gameName.contains(queryLower)) {
            score += 100.0;
        }

        // bonus for higher ratings
        if (game.getRating() != null) {
            score += game.getRating() * 10;
        }

        // bonus for more rating counts (popular games)
        if (game.getRatingsCount() != null) {
            score += Math.min(game.getRatingsCount() / 10.0, 50.0);
        }

        // maybe penalty for DLC/expansion keywords idk
        // if (containsDlcKeywords(gameName)) {
            //score -= 100.0;
        // }

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
