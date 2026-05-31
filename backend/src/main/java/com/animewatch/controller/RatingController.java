package com.animewatch.controller;

import com.animewatch.dto.RatingDTOs;
import com.animewatch.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName());
    }

    @GetMapping("/anime/{animeId}")
    public ResponseEntity<List<RatingDTOs.RatingResponse>> getRatingsForAnime(@PathVariable UUID animeId) {
        return ResponseEntity.ok(ratingService.getRatingsForAnime(animeId));
    }

    @PostMapping
    public ResponseEntity<RatingDTOs.RatingResponse> createRating(@RequestBody RatingDTOs.CreateRatingRequest request) {
        UUID userId = getCurrentUserId();
        return ResponseEntity.status(201).body(ratingService.createRating(userId, request));
    }
}
