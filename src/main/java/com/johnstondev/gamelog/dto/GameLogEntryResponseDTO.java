package com.johnstondev.gamelog.dto;

import com.johnstondev.gamelog.model.GameStatus;
import java.time.LocalDateTime;

public class GameLogEntryResponseDTO {

    private Long id;
    private Long rawgGameId;
    private String gameTitle;
    private String gameCoverImage;
    private GameStatus status;
    private Integer rating;
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;

    public GameLogEntryResponseDTO() {}

    public GameLogEntryResponseDTO(Long id, Long rawgGameId, String gameTitle, String gameCoverImage,
                                   GameStatus status, Integer rating, LocalDateTime addedAt, LocalDateTime updatedAt) {
        this.id = id;
        this.rawgGameId = rawgGameId;
        this.gameTitle = gameTitle;
        this.gameCoverImage = gameCoverImage;
        this.status = status;
        this.rating = rating;
        this.addedAt = addedAt;
        this.updatedAt = updatedAt;
    }

    // getters and setters !!!!!

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRawgGameId() { return rawgGameId; }
    public void setRawgGameId(Long rawgGameId) { this.rawgGameId = rawgGameId; }

    public String getGameTitle() { return gameTitle; }
    public void setGameTitle(String gameTitle) { this.gameTitle = gameTitle; }

    public String getGameCoverImage() { return gameCoverImage; }
    public void setGameCoverImage(String gameCoverImage) { this.gameCoverImage = gameCoverImage; }

    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) { this.status = status; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}