package com.picus.core.support.domain.model.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnswerStatus {

    PENDING("답변중"),
    ANSWERED("답변완료");

    private final String text;
}
