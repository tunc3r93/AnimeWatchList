package com.animewatch.service;

import com.animewatch.domain.model.Rating;
import com.animewatch.domain.model.User;
import com.animewatch.dto.RatingDTOs;
import com.animewatch.exception.NotFoundException;
import com.animewatch.repository.AnimeRepository;
import com.animewatch.repository.RatingRepository;
import com.animewatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final AnimeRepository animeRepository;

    @Transactional(readOnly = true)
    public List<RatingDTOs.RatingResponse> getRatingsForAnime(UUID animeId) {
        return ratingRepository.findAllByAnimeId(animeId).stream()
            .map(this::toRatingResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public RatingDTOs.RatingResponse createRating(UUID userId, RatingDTOs.CreateRatingRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));
        var anime = animeRepository.findById(request.getAnimeId())
            .orElseThrow(() -> new NotFoundException("Anime not found"));

        var existing = ratingRepository.findByUserIdAndAnimeId(userId, request.getAnimeId());
        if (existing.isPresent()) {
            Rating rating = existing.get();
            rating.setScore(request.getScore());
            rating.setComment(request.getComment());
            Rating updated = ratingRepository.save(rating);
            return toRatingResponse(updated);
        }

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setAnime(anime);
        rating.setScore(request.getScore());
        rating.setComment(request.getComment());

        Rating saved = ratingRepository.save(rating);
        return toRatingResponse(saved);
    }

    private RatingDTOs.RatingResponse toRatingResponse(Rating rating) {
        return RatingDTOs.RatingResponse.builder()
            .id(rating.getId())
            .userName(rating.getUser().getFirstName() + " " + rating.getUser().getLastName())
            .score(rating.getScore())
            .comment(rating.getComment())
            .build();
    }
}
