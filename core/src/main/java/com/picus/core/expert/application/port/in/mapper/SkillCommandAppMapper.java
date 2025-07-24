package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.request.UpdateSkillAppReq;
import com.picus.core.expert.domain.model.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillCommandAppMapper {

    public Skill toDomain(UpdateSkillAppReq command) {
        return Skill.builder()
                .skillNo(command.skillNo())
                .skillType(command.skillType())
                .content(command.content())
                .build();
    }
}
