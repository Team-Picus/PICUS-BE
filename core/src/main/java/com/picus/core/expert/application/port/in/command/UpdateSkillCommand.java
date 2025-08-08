package com.picus.core.expert.application.port.in.command;

import com.picus.core.expert.domain.vo.SkillType;
import lombok.Builder;

@Builder
public record UpdateSkillCommand(
        String skillNo,
        SkillType skillType,
        String content,
        ChangeStatus changeStatus
) {
}
