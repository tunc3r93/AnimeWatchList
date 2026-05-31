package com.animewatch.domain.model;

public class TenantContext {
    private static final ThreadLocal<Tenant> tenantHolder = new ThreadLocal<>();

    public static void setTenant(Tenant tenant) {
        tenantHolder.set(tenant);
    }

    public static Tenant getTenant() {
        return tenantHolder.get();
    }

    public static void clear() {
        tenantHolder.remove();
    }
}
