package com.picus.core.expert.domain.model;

import com.picus.core.expert.domain.model.vo.SkillType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Skill {
    private SkillType skillType;
    private String content;
}
