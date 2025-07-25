package com.picus.core.expert.application.port.in.command;

import com.picus.core.expert.domain.model.vo.SkillType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
public record SkillCommand(
        String skillNo,
        SkillType skillType,
        String content,
        ChangeStatus changeStatus
) {
}
