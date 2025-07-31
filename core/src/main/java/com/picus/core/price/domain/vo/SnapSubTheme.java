package com.picus.core.price.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SnapSubTheme {
    PROFILE("개인 프로필"),
    FRIENDSHIP("우정"),
    FAMILY("가족"),
    ADMISSION("입학");

    private final String text;
}
