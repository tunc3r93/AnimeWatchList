package com.animewatch.controller;

import com.animewatch.dto.WatchlistDTOs;
import com.animewatch.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName());
    }

    @GetMapping
    public ResponseEntity<Page<WatchlistDTOs.WatchlistResponse>> getWatchlist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        UUID userId = getCurrentUserId();
        return ResponseEntity.ok(watchlistService.getWatchlist(userId, page, size, status));
    }

    @PostMapping
    public ResponseEntity<WatchlistDTOs.WatchlistResponse> addToWatchlist(
            @RequestBody WatchlistDTOs.AddToWatchlistRequest request) {
        UUID userId = getCurrentUserId();
        return ResponseEntity.status(201).body(watchlistService.addToWatchlist(userId, request));
    }

    @PutMapping("/{animeId}")
    public ResponseEntity<WatchlistDTOs.WatchlistResponse> updateWatchlistStatus(
            @PathVariable UUID animeId,
            @RequestBody WatchlistDTOs.UpdateStatusRequest request) {
        UUID userId = getCurrentUserId();
        return ResponseEntity.ok(watchlistService.updateStatus(userId, animeId, request));
    }

    @DeleteMapping("/{animeId}")
    public ResponseEntity<Void> removeFromWatchlist(@PathVariable UUID animeId) {
        UUID userId = getCurrentUserId();
        watchlistService.removeFromWatchlist(userId, animeId);
        return ResponseEntity.noContent().build();
    }
}
