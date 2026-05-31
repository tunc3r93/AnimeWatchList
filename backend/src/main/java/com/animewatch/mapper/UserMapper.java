package com.animewatch.mapper;

import com.animewatch.domain.model.User;
import com.animewatch.domain.model.Tenant;
import com.animewatch.dto.AuthDTOs.*;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .role(user.getRole().toString())
            .createdAt(user.getCreatedAt())
            .build();
    }

    public TenantResponse toTenantResponse(Tenant tenant) {
        return TenantResponse.builder()
            .id(tenant.getId())
            .name(tenant.getName())
            .slug(tenant.getSlug())
            .createdAt(tenant.getCreatedAt())
            .build();
    }
}
