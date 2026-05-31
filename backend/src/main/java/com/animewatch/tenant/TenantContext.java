package com.animewatch.tenant;

import java.util.UUID;

public class TenantContext {
    private static final ThreadLocal<UUID> tenantId = new ThreadLocal<>();

    public static void setCurrentTenantId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("TenantId cannot be null");
        }
        tenantId.set(id);
    }

    public static UUID getCurrentTenantId() {
        UUID id = tenantId.get();
        if (id == null) {
            throw new IllegalStateException(
                "TenantContext not set. Make sure you are within an authenticated request."
            );
        }
        return id;
    }

    public static void clear() {
        tenantId.remove();
    }
}
