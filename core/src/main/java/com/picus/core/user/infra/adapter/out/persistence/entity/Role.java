package com.picus.core.user.infra.adapter.out.persistence.entity;

public enum Role {
    CLIENT,
    EXPERT;

    public String toAuthority() {
        return "ROLE_" + this.name();
    }
}
