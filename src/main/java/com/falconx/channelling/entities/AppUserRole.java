package com.falconx.channelling.entities;

import org.springframework.security.core.GrantedAuthority;

public enum AppUserRole implements GrantedAuthority {

    ROLE_ADMIN, ROLE_RECEPTIONIST, ROLE_PHARMACIST, ROLE_DOCTOR, ROLE_PATIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
