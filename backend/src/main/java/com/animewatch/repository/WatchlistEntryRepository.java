package com.animewatch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.animewatch.domain.model.WatchlistEntry;
import com.animewatch.domain.model.WatchlistStatus;
import com.animewatch.tenant.TenantContext;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WatchlistEntryRepository extends JpaRepository<WatchlistEntry, UUID> {

    Optional<WatchlistEntry> findByUserIdAndAnimeId(UUID userId, UUID animeId);

    @Query("SELECT w FROM WatchlistEntry w WHERE w.user.id = :userId AND w.user.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()}")
    Page<WatchlistEntry> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT w FROM WatchlistEntry w WHERE w.user.id = :userId " +
           "AND w.user.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()} " +
           "AND w.status = :status")
    Page<WatchlistEntry> findByUserIdAndStatus(
        @Param("userId") UUID userId,
        @Param("status") WatchlistStatus status,
        Pageable pageable
    );
}
