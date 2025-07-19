package com.picus.core.user.domain.model;

import lombok.Getter;

@Getter
public enum Role {
    CLIENT("ROLE_CLIENT"),
    EXPERT("ROLE_EXPERT");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
