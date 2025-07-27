package com.picus.core.expert.domain;

import com.picus.core.expert.domain.vo.SkillType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class Skill {
    private String skillNo;
    private SkillType skillType;
    private String content;

    public void updateSkill(SkillType skillType, String content) {
        if(skillType != null)
            this.skillType = skillType;
        if(content != null)
            this.content = content;
    }
}
