package com.picus.core.moodboard.adapter.out.persistence.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MoodboardId implements Serializable {

    @EqualsAndHashCode.Include
    private String userNo;

    @EqualsAndHashCode.Include
    private String postNo;

    public MoodboardId(String userNo, String postNo) {
        this.userNo = userNo;
        this.postNo = postNo;
    }
}