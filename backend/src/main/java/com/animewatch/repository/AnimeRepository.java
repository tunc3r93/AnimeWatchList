package com.animewatch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.animewatch.domain.model.Anime;
import com.animewatch.domain.model.AnimeStatus;
import com.animewatch.tenant.TenantContext;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, UUID> {

    @Query("SELECT a FROM Anime a WHERE a.id = :id AND a.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()}")
    Optional<Anime> findById(@Param("id") UUID id);

    @Query("SELECT a FROM Anime a WHERE a.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()}")
    Page<Anime> findAll(Pageable pageable);

    @Query("SELECT a FROM Anime a WHERE a.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()} " +
           "AND (LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(a.genre) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Anime> search(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT a FROM Anime a WHERE a.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()} " +
           "AND a.status = :status")
    Page<Anime> findByStatus(@Param("status") AnimeStatus status, Pageable pageable);

    @Query("SELECT a FROM Anime a WHERE a.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()} " +
           "AND a.genre LIKE CONCAT('%', :genre, '%')")
    Page<Anime> findByGenre(@Param("genre") String genre, Pageable pageable);
}
