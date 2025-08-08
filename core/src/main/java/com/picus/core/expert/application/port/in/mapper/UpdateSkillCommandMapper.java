package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.command.UpdateSkillCommand;
import com.picus.core.expert.domain.Skill;
import org.springframework.stereotype.Component;

@Component
public class UpdateSkillCommandMapper {

    public Skill toDomain(UpdateSkillCommand command) {
        return Skill.builder()
                .skillNo(command.skillNo())
                .skillType(command.skillType())
                .content(command.content())
                .build();
    }
}
