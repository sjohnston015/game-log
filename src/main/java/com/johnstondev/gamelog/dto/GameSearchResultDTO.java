package com.johnstondev.gamelog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameSearchResultDTO {

    private Long id;
    private String name;
    private String slug;

    @JsonProperty("background_image")
    private String backgroundImage;
    private String[] platforms;
    private String released;
    private Double rating;

    @JsonProperty("ratings_count")
    private Integer ratingsCount;
    private Integer metacritic;

    public GameSearchResultDTO() {} // default constructor for DTO

    public GameSearchResultDTO(Long id, String name, String slug, String backgroundImage,
                               String[] platforms, String released, Double rating, Integer ratingsCount,
                               Integer metacritic) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.backgroundImage = backgroundImage;
        this.platforms = platforms;
        this.released = released;
        this.rating = rating;
        this.ratingsCount = ratingsCount;
        this.metacritic = metacritic;
    }

    // getters and setters !!!!! ┐(︶▽︶)┌

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getBackgroundImage() { return backgroundImage; }
    public void setBackgroundImage(String backgroundImage) { this.backgroundImage = backgroundImage; }

    public String[] getPlatforms() { return platforms; }
    public void setPlatforms(String[] platforms) { this.platforms = platforms; }

    public String getReleased() { return released; }
    public void setReleased(String released) { this.released = released; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getRatingsCount() { return ratingsCount; }
    public void setRatingsCount(Integer ratingsCount) { this.ratingsCount = ratingsCount; }

    public Integer getMetacritic() { return metacritic; }
    public void setMetacritic(Integer metacritic) { this.metacritic = metacritic; }
}
