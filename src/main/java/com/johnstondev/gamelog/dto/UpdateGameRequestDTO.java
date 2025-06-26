package com.johnstondev.gamelog.dto;

import com.johnstondev.gamelog.model.GameStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UpdateGameRequestDTO {

    private GameStatus status;

    @Min(value = 1, message = "Rating must be between 1 and 10")
    @Max(value = 10, message = "Rating must be between 1 and 10")
    private Integer rating;

    public UpdateGameRequestDTO() {}

    public UpdateGameRequestDTO(GameStatus status, Integer rating) {
        this.status = status;
        this.rating = rating;
    }

    // getters and setters !!!!!

    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) { this.status = status; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
}