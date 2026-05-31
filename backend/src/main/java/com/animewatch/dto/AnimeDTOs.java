package com.animewatch.dto;

import com.animewatch.domain.model.AnimeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class AnimeDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnimeResponse {
        private UUID id;
        private String title;
        private String description;
        private String coverUrl;
        private String genre;
        private Integer episodeCount;
        private AnimeStatus status;
        private Integer releaseYear;
        private Double avgRating;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAnimeRequest {
        private String title;
        private String description;
        private String coverUrl;
        private String genre;
        private Integer episodeCount;
        private AnimeStatus status;
        private Integer releaseYear;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAnimeRequest {
        private String title;
        private String description;
        private String coverUrl;
        private String genre;
        private Integer episodeCount;
        private AnimeStatus status;
        private Integer releaseYear;
    }
}
