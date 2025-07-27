package com.picus.core.expert.domain.vo;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SkillType {
    CAMERA("카메라"),
    LIGHT("조명"),
    EDIT("편집");

    private final String text;
}
