package com.picus.core.post.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostMoodType {

    URBAN("도회적인"),
    EXPERIMENTAL("실험적인"),
    INTENSE("강렬한"),
    MODERN("모던"),
    VINTAGE("빈티지"),
    CINEMATIC("시네마틱"),
    COOL("차가운"),
    COZY("포근한"),
    DREAMY("몽환적인");

    private final String text;
}
