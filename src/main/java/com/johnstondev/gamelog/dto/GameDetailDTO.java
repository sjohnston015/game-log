package com.johnstondev.gamelog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.johnstondev.gamelog.model.GameStatus;

import java.util.List;

public class GameDetailDTO {

    private Long id;
    private String name;
    private String slug;
    private String description;

    @JsonProperty("background_image")
    private String backgroundImage;
    private String released;
    private Double rating;

    @JsonProperty("ratings_count")
    private Integer ratingsCount;
    private Integer metacritic;

    @JsonProperty("playtime")
    private Integer averagePlaytime;
    private List<Genre> genres;
    private List<Platform> platforms;

    // inner classes for those bitchass nested JSON objects RAWG has (it's not bad at all)
    public static class Genre {
        private Long id;
        private String name;
        private String slug;

        public Genre() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
    }

    public static class Platform {
        private PlatformDetail platform;

        public static class PlatformDetail {
            private Long id;
            private String name;
            private String slug;

            public PlatformDetail() {}

            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }

            public String getName() { return name; }
            public void setName(String name) { this.name = name; }

            public String getSlug() { return slug; }
            public void setSlug(String slug) { this.slug = slug; }
        }

        public Platform() {}

        public PlatformDetail getPlatform() { return platform; }
        public void setPlatform(PlatformDetail platform) { this.platform = platform; }
    }

    public GameDetailDTO() {}

    // getters and setters !!!!! (¯▿¯)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBackgroundImage() { return backgroundImage; }
    public void setBackgroundImage(String backgroundImage) { this.backgroundImage = backgroundImage; }

    public String getReleased() { return released; }
    public void setReleased(String released) { this.released = released; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getRatingsCount() { return ratingsCount; }
    public void setRatingsCount(Integer ratingsCount) { this.ratingsCount = ratingsCount; }

    public Integer getMetacritic() { return metacritic; }
    public void setMetacritic(Integer metacritic) { this.metacritic = metacritic; }

    public Integer getAveragePlaytime() { return averagePlaytime; }
    public void setAveragePlaytime(Integer averagePlaytime) { this.averagePlaytime = averagePlaytime; }

    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }

    public List<Platform> getPlatforms() { return platforms; }
    public void setPlatforms(List<Platform> platforms) { this.platforms = platforms; }
}
