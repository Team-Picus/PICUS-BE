package com.picus.core.post.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostThemeType {

    FASHION("패션"),
    BEAUTY("뷰티"),
    EVENT("행사"),
    WEDDING("웨딩"),
    SNAP("스냅");

    private final String text;
}
