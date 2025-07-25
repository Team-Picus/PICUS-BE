package com.picus.core.moodboard.adapter.out.persistence.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MoodboardId implements Serializable {

    @EqualsAndHashCode.Include
    private String user;

    @EqualsAndHashCode.Include
    private String post;

    public MoodboardId(String user, String post) {
        this.user = user;
        this.post = post;
    }
}