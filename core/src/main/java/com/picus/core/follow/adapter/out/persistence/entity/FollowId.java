package com.picus.core.follow.adapter.out.persistence.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FollowId implements Serializable {

    @EqualsAndHashCode.Include
    private String user;

    @EqualsAndHashCode.Include
    private String expert;

    public FollowId(String user, String expert) {
        this.user = user;
        this.expert = expert;
    }
}