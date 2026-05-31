package com.animewatch.service;

import com.animewatch.domain.model.Anime;
import com.animewatch.domain.model.TenantContext;
import com.animewatch.dto.AnimeDTOs;
import com.animewatch.exception.NotFoundException;
import com.animewatch.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    @Transactional(readOnly = true)
    public Page<AnimeDTOs.AnimeResponse> getAllAnime(int page, int size, String search, String genre, String status) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Anime> animeList = animeRepository.findAll(pageable);
        return animeList.map(this::toAnimeResponse);
    }

    @Transactional(readOnly = true)
    public AnimeDTOs.AnimeResponse getAnimeById(UUID id) {
        Anime anime = animeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Anime not found"));
        return toAnimeResponse(anime);
    }

    @Transactional
    public AnimeDTOs.AnimeResponse createAnime(AnimeDTOs.CreateAnimeRequest request) {
        Anime anime = new Anime();
        anime.setTitle(request.getTitle());
        anime.setDescription(request.getDescription());
        anime.setCoverUrl(request.getCoverUrl());
        anime.setGenre(request.getGenre());
        anime.setEpisodeCount(request.getEpisodeCount());
        anime.setStatus(request.getStatus());
        anime.setReleaseYear(request.getReleaseYear());
        anime.setTenant(TenantContext.getTenant());

        Anime saved = animeRepository.save(anime);
        return toAnimeResponse(saved);
    }

    @Transactional
    public AnimeDTOs.AnimeResponse updateAnime(UUID id, AnimeDTOs.UpdateAnimeRequest request) {
        Anime anime = animeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Anime not found"));

        if (request.getTitle() != null) anime.setTitle(request.getTitle());
        if (request.getDescription() != null) anime.setDescription(request.getDescription());
        if (request.getCoverUrl() != null) anime.setCoverUrl(request.getCoverUrl());
        if (request.getGenre() != null) anime.setGenre(request.getGenre());
        if (request.getEpisodeCount() != null) anime.setEpisodeCount(request.getEpisodeCount());
        if (request.getStatus() != null) anime.setStatus(request.getStatus());

        Anime updated = animeRepository.save(anime);
        return toAnimeResponse(updated);
    }

    @Transactional
    public void deleteAnime(UUID id) {
        Anime anime = animeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Anime not found"));
        animeRepository.delete(anime);
    }

    private AnimeDTOs.AnimeResponse toAnimeResponse(Anime anime) {
        double avgRating = 0.0;
        if (anime.getAvgRating() != null) {
            avgRating = anime.getAvgRating().doubleValue();
        }
        
        return AnimeDTOs.AnimeResponse.builder()
            .id(anime.getId())
            .title(anime.getTitle())
            .description(anime.getDescription())
            .coverUrl(anime.getCoverUrl())
            .genre(anime.getGenre())
            .episodeCount(anime.getEpisodeCount())
            .status(anime.getStatus())
            .releaseYear(anime.getReleaseYear())
            .avgRating(avgRating)
            .build();
    }
}
