package com.animewatch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.animewatch.domain.model.Rating;
import com.animewatch.tenant.TenantContext;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {

    Optional<Rating> findByUserIdAndAnimeId(UUID userId, UUID animeId);

    @Query("SELECT r FROM Rating r WHERE r.anime.id = :animeId AND r.anime.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()}")
    Page<Rating> findByAnimeId(@Param("animeId") UUID animeId, Pageable pageable);

    @Query("SELECT r FROM Rating r WHERE r.anime.id = :animeId AND r.anime.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()}")
    java.util.List<Rating> findAllByAnimeId(@Param("animeId") UUID animeId);

    @Query("SELECT r FROM Rating r WHERE r.user.id = :userId AND r.user.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()}")
    Page<Rating> findByUserId(@Param("userId") UUID userId, Pageable pageable);
}
