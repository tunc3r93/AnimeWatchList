package com.animewatch.service;

import com.animewatch.domain.model.*;
import com.animewatch.dto.AuthDTOs;
import com.animewatch.exception.*;
import com.animewatch.mapper.UserMapper;
import com.animewatch.repository.*;
import com.animewatch.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final InvitationRepository invitationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    private static final String PASSWORD_PATTERN =
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @Transactional(readOnly = true)
    public AuthDTOs.AuthResponse authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("Login attempt with non-existent email: {}", email);
                return new BadCredentialsException("Invalid credentials");
            });

        // For testing: compare plaintext password
        if (!password.equals(user.getPassword())) {
            log.warn("Login attempt with wrong password for email: {}", email);
            throw new BadCredentialsException("Invalid credentials");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        log.info("User {} logged in successfully", email);

        return AuthDTOs.AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(userMapper.toUserResponse(user))
            .tenant(userMapper.toTenantResponse(user.getTenant()))
            .build();
    }

    @Transactional
    public AuthDTOs.UserResponse register(AuthDTOs.RegisterRequest request) {
        Invitation invitation = invitationRepository.findByCode(request.getInvitationCode())
            .orElseThrow(() -> new NotFoundException("Invitation not found"));

        if (invitation.getUsedAt() != null) {
            throw new ConflictException("Invitation already used");
        }

        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Invitation expired");
        }

        validatePassword(request.getPassword());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(invitation.getRole());
        user.setTenant(invitation.getTenant());

        User savedUser = userRepository.save(user);

        invitation.setUsedAt(LocalDateTime.now());
        invitation.setUsedBy(savedUser);
        invitationRepository.save(invitation);

        log.info("User {} registered successfully for tenant {}",
            request.getEmail(), invitation.getTenant().getId());

        return userMapper.toUserResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public AuthDTOs.AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(UUID.fromString(userId))
            .orElseThrow(() -> new NotFoundException("User not found"));

        String accessToken = jwtTokenProvider.generateAccessToken(user);

        return AuthDTOs.AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(userMapper.toUserResponse(user))
            .tenant(userMapper.toTenantResponse(user.getTenant()))
            .build();
    }

    private void validatePassword(String password) {
        if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            throw new ValidationException(
                "Password must be at least 8 characters long and contain " +
                "at least one uppercase letter, one lowercase letter, one digit, " +
                "and one special character (@$!%*?&)"
            );
        }
    }
}
