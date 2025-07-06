package com.picus.core.chat.domain.model.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageType {
    TEXT("텍스트"),
    RESERVATION("예약"),
    IMAGE("사진"),
    FILE("파일");

    private final String text;
}
