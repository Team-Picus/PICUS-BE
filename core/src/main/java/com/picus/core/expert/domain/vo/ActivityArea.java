package com.picus.core.expert.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityArea {
    SEOUL_GANGNAMGU("서울 강남구"),
    SEOUL_GANGDONGGU("서울 강동구"),
    SEOUL_GANGBUKGU("서울 강북구"),
    SEOUL_GANGSEOGU("서울 강서구"),
    SEOUL_GWANAKGU("서울 관악구");
    // TODO: 나머지 지역 추가

    private final String text;
}
