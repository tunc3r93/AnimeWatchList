package com.animewatch.service;

import com.animewatch.domain.model.Invitation;
import com.animewatch.domain.model.Tenant;
import com.animewatch.domain.model.TenantContext;
import com.animewatch.domain.model.User;
import com.animewatch.domain.model.UserRole;
import com.animewatch.dto.AdminDTOs;
import com.animewatch.repository.InvitationRepository;
import com.animewatch.repository.TenantRepository;
import com.animewatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final InvitationRepository invitationRepository;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<AdminDTOs.InvitationResponse> getInvitations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var invitations = invitationRepository.findAll();
        return new PageImpl<>(
            invitations.stream()
                .map(this::toInvitationResponse)
                .collect(Collectors.toList()),
            pageable,
            invitations.size()
        );
    }

    @Transactional
    public AdminDTOs.InvitationResponse createInvitation(AdminDTOs.CreateInvitationRequest request, UUID userId) {
        Tenant tenant = TenantContext.getTenant();
        User createdBy = userRepository.findById(userId).orElseThrow();

        Invitation invitation = new Invitation();
        invitation.setCode(UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        invitation.setInvitedEmail(request.getInvitedEmail());
        invitation.setRole(request.getRole() != null ? request.getRole() : UserRole.USER);
        invitation.setTenant(tenant);
        invitation.setCreatedBy(createdBy);
        invitation.setExpiresAt(LocalDateTime.now().plusDays(request.getExpiryDays() != null ? request.getExpiryDays() : 30));

        Invitation saved = invitationRepository.save(invitation);
        return toInvitationResponse(saved);
    }

    private AdminDTOs.InvitationResponse toInvitationResponse(Invitation invitation) {
        return AdminDTOs.InvitationResponse.builder()
            .code(invitation.getCode())
            .invitedEmail(invitation.getInvitedEmail())
            .role(invitation.getRole())
            .status(invitation.getUsedAt() == null ? "ACTIVE" : "USED")
            .createdAt(invitation.getCreatedAt())
            .expiresAt(invitation.getExpiresAt())
            .build();
    }
}
