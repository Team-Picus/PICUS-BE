package com.picus.core.domain.shared.category.entity;

import lombok.Getter;

@Getter
public enum Category {
    // 장소 카테고리
    STUDIO("스튜디오", CategoryType.LOCATION),
    OUTSIDE("출강", CategoryType.LOCATION),

    // 테마 카테고리
    COUPLE("연인과 함께", CategoryType.THEME),
    FAMILY("가족과 함께", CategoryType.THEME),
    PROFILE("개인프로필", CategoryType.THEME),

    // 분위기 카테고리
    DREAMY("몽환적", CategoryType.MOOD),
    DARK("어두운", CategoryType.MOOD),
    BRIGHT("밝은", CategoryType.MOOD),
    CLASSIC("클래식", CategoryType.MOOD),
    COLD("차가운", CategoryType.MOOD),
    WARM("따뜻한", CategoryType.MOOD),
    VINTAGE("빈티지", CategoryType.MOOD),
    NATURAL("자연적인", CategoryType.MOOD),
    URBAN("도시적인", CategoryType.MOOD);

    private final String displayName;
    private final CategoryType type;

    Category(String displayName, CategoryType type) {
        this.displayName = displayName;
        this.type = type;
    }
}
