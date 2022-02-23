package com.zzuni.zzunistory.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    public static final String ROLE_PREFIX = "ROLE_";
    private String description;
}
