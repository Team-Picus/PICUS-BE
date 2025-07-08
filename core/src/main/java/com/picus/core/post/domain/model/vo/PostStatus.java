package com.picus.core.post.domain.model.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {
    INITIALIZED("초기화"),
    COMPLETED("작성완료");

    private final String text;

}
