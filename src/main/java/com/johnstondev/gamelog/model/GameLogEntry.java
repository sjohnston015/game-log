package com.johnstondev.gamelog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/*
* Class that ties it all together! Represents a Table in my database (PostgreSQL) that represents
* the games
*
*/

@Entity
@Table(name = "user_game_log")
public class GameLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "rawg_game_id", nullable = false)
    private Long rawgId;

    @Column(name = "game_title", nullable = false)
    private String gameTitle;

    @Column(name = "game_cover_image")
    private String gameCoverImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @Column
    private Integer rating; // 1-10, nullable!!! Integer not "int" so nullable!!! bleh >.<

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public GameLogEntry() {}

    public GameLogEntry(User user, Long rawgId, String gameTitle, GameStatus status) {
        this.user = user;
        this.rawgId = rawgId;
        this.gameTitle = gameTitle;
        this.status = status;
    }

    // getters and setters !!!!! ({O.-})b

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Long getRawgId() { return rawgId; }
    public void setRawgId(Long rawgId) { this.rawgId = rawgId; }

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

    // @PrePersist and @PreUpdate for automatic timestamps!!!!

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        addedAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
