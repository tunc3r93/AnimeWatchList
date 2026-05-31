package com.animewatch.controller;

import com.animewatch.dto.AnimeDTOs;
import com.animewatch.service.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/anime")
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<AnimeDTOs.AnimeResponse>> getAnime(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(animeService.getAllAnime(page, size, search, genre, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeDTOs.AnimeResponse> getAnimeById(@PathVariable UUID id) {
        return ResponseEntity.ok(animeService.getAnimeById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnimeDTOs.AnimeResponse> createAnime(@RequestBody AnimeDTOs.CreateAnimeRequest request) {
        return ResponseEntity.status(201).body(animeService.createAnime(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnimeDTOs.AnimeResponse> updateAnime(@PathVariable UUID id, @RequestBody AnimeDTOs.UpdateAnimeRequest request) {
        return ResponseEntity.ok(animeService.updateAnime(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAnime(@PathVariable UUID id) {
        animeService.deleteAnime(id);
        return ResponseEntity.noContent().build();
    }
}
