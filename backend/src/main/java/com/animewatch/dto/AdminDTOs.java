package com.animewatch.dto;

import com.animewatch.domain.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AdminDTOs {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvitationResponse {
        private String code;
        private String invitedEmail;
        private UserRole role;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime expiresAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateInvitationRequest {
        private String invitedEmail;
        private UserRole role;
        private Integer expiryDays;
    }
}
