package com.johnstondev.gamelog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameSearchResultDTO {

    private Long id;
    private String name;
    private String slug;

    @JsonProperty("background_image")
    private String backgroundImage;

    @JsonProperty("platforms")
    private List<PlatformInfo> platforms;

    private String released;
    private Double rating;

    @JsonProperty("ratings_count")
    private Integer ratingsCount;
    private Integer metacritic;

    // inner class to get the "platforms" from the JSON response from RAWG
    public static class PlatformInfo {
        private PlatformDetail platform;

        public static class PlatformDetail {
            private String name;

            public PlatformDetail() {}

            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
        }

        public PlatformInfo() {}

        public PlatformDetail getPlatform() { return platform; }
        public void setPlatform(PlatformDetail platform) { this.platform = platform; }
    }

    // default constructor for DTO
    public GameSearchResultDTO() {}

    // getters and setters !!!!! ┐(︶▽︶)┌

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getBackgroundImage() { return backgroundImage; }
    public void setBackgroundImage(String backgroundImage) { this.backgroundImage = backgroundImage; }

    public List<PlatformInfo> getPlatforms() { return platforms; }
    public void setPlatforms(List<PlatformInfo> platforms) { this.platforms = platforms; }

    public String getReleased() { return released; }
    public void setReleased(String released) { this.released = released; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getRatingsCount() { return ratingsCount; }
    public void setRatingsCount(Integer ratingsCount) { this.ratingsCount = ratingsCount; }

    public Integer getMetacritic() { return metacritic; }
    public void setMetacritic(Integer metacritic) { this.metacritic = metacritic; }
}
