package com.picus.core.post.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SpaceType {
    INDOOR("실내"),
    OUTDOOR("실외");

    private final String text;
}
