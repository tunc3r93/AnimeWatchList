package com.animewatch.controller;

import com.animewatch.dto.AdminDTOs;
import com.animewatch.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName());
    }

    @GetMapping("/invitations")
    public ResponseEntity<Page<AdminDTOs.InvitationResponse>> getInvitations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminService.getInvitations(page, size));
    }

    @PostMapping("/invitations")
    public ResponseEntity<AdminDTOs.InvitationResponse> createInvitation(
            @RequestBody AdminDTOs.CreateInvitationRequest request) {
        UUID userId = getCurrentUserId();
        return ResponseEntity.status(201).body(adminService.createInvitation(request, userId));
    }
}
