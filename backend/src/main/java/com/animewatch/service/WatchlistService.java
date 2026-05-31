package com.animewatch.service;

import com.animewatch.domain.model.User;
import com.animewatch.domain.model.WatchlistEntry;
import com.animewatch.domain.model.WatchlistStatus;
import com.animewatch.dto.WatchlistDTOs;
import com.animewatch.exception.NotFoundException;
import com.animewatch.repository.AnimeRepository;
import com.animewatch.repository.UserRepository;
import com.animewatch.repository.WatchlistEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final WatchlistEntryRepository watchlistRepository;
    private final UserRepository userRepository;
    private final AnimeRepository animeRepository;

    @Transactional(readOnly = true)
    public Page<WatchlistDTOs.WatchlistResponse> getWatchlist(UUID userId, int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size);
        
        Page<WatchlistEntry> entries;
        if (status != null && !status.isEmpty()) {
            try {
                WatchlistStatus watchStatus = WatchlistStatus.valueOf(status);
                entries = watchlistRepository.findByUserIdAndStatus(userId, watchStatus, pageable);
            } catch (IllegalArgumentException e) {
                entries = watchlistRepository.findByUserId(userId, pageable);
            }
        } else {
            entries = watchlistRepository.findByUserId(userId, pageable);
        }
        
        return entries.map(this::toWatchlistResponse);
    }

    @Transactional
    public WatchlistDTOs.WatchlistResponse addToWatchlist(UUID userId, WatchlistDTOs.AddToWatchlistRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));
        var anime = animeRepository.findById(request.getAnimeId())
            .orElseThrow(() -> new NotFoundException("Anime not found"));

        WatchlistEntry entry = new WatchlistEntry();
        entry.setUser(user);
        entry.setAnime(anime);
        entry.setStatus(WatchlistStatus.PLAN_TO_WATCH);

        var saved = watchlistRepository.save(entry);
        return toWatchlistResponse(saved);
    }

    @Transactional
    public WatchlistDTOs.WatchlistResponse updateStatus(UUID userId, UUID animeId, WatchlistDTOs.UpdateStatusRequest request) {
        var entry = watchlistRepository.findByUserIdAndAnimeId(userId, animeId)
            .orElseThrow(() -> new NotFoundException("Watchlist entry not found"));
        entry.setStatus(request.getStatus());
        var updated = watchlistRepository.save(entry);
        return toWatchlistResponse(updated);
    }

    @Transactional
    public void removeFromWatchlist(UUID userId, UUID animeId) {
        var entry = watchlistRepository.findByUserIdAndAnimeId(userId, animeId)
            .orElseThrow(() -> new NotFoundException("Watchlist entry not found"));
        watchlistRepository.delete(entry);
    }

    private WatchlistDTOs.WatchlistResponse toWatchlistResponse(WatchlistEntry entry) {
        return WatchlistDTOs.WatchlistResponse.builder()
            .id(entry.getAnime().getId())
            .title(entry.getAnime().getTitle())
            .coverUrl(entry.getAnime().getCoverUrl())
            .status(entry.getStatus())
            .genre(entry.getAnime().getGenre())
            .build();
    }
}
