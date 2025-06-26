package com.johnstondev.gamelog.dto;

import com.johnstondev.gamelog.model.GameStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddGameRequestDTO {

    @NotNull(message = "RAWG game ID is required")
    private Long rawgGameId;

    @NotNull(message = "Status is required")
    private GameStatus status;

    @Min(value = 1, message = "Rating must be between 1 and 10")
    @Max(value = 10, message = "Rating must be between 1 and 10")
    private Integer rating;

    public AddGameRequestDTO() {}

    public AddGameRequestDTO(Long rawgGameId, GameStatus status, Integer rating) {
        this.rawgGameId = rawgGameId;
        this.status = status;
        this.rating = rating;
    }

    // getters and setters !!!! >.< !!!!!

    public Long getRawgGameId() { return rawgGameId; }
    public void setRawgGameId(Long rawgGameId) { this.rawgGameId = rawgGameId; }

    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) { this.status = status; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
}