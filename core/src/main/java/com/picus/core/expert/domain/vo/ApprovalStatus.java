package com.picus.core.expert.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApprovalStatus {
    PENDING("요청중"),
    APPROVAL("승인완료"),
    REJECT("승인거부");

    private final String text;
}
