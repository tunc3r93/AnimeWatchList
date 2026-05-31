package com.animewatch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class RatingDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingResponse {
        private UUID id;
        private String userName;
        private Integer score;
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRatingRequest {
        private UUID animeId;
        private Integer score;
        private String comment;
    }
}
