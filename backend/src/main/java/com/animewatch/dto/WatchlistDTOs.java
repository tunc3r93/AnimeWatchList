package com.animewatch.dto;

import com.animewatch.domain.model.WatchlistStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class WatchlistDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WatchlistResponse {
        private UUID id;
        private String title;
        private String coverUrl;
        private String genre;
        private WatchlistStatus status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddToWatchlistRequest {
        private UUID animeId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateStatusRequest {
        private WatchlistStatus status;
    }
}
