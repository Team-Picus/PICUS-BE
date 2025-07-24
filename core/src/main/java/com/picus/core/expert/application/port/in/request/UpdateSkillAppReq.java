package com.picus.core.expert.application.port.in.request;

import com.picus.core.expert.domain.model.vo.SkillType;
import lombok.Builder;

@Builder
public record UpdateSkillAppReq(
        String skillNo,
        SkillType skillType,
        String content,
        ChangeStatus changeStatus
) {
}
