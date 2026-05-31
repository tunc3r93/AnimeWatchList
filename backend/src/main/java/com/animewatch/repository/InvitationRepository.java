package com.animewatch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.animewatch.domain.model.Invitation;
import com.animewatch.tenant.TenantContext;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {

    Optional<Invitation> findByCode(String code);

    @Query("SELECT i FROM Invitation i WHERE i.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()}")
    Page<Invitation> findAll(Pageable pageable);

    @Query("SELECT i FROM Invitation i WHERE i.tenant.id = ?#{T(com.animewatch.tenant.TenantContext).getCurrentTenantId()} " +
           "AND i.usedAt IS NULL AND i.expiresAt > CURRENT_TIMESTAMP")
    Page<Invitation> findActiveInvitations(Pageable pageable);
}
