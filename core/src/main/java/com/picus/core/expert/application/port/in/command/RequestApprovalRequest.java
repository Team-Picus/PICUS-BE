package com.picus.core.expert.application.port.in.command;

import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.Portfolio;
import lombok.Builder;

import java.util.List;

@Builder
public record RequestApprovalRequest(
        String activityCareer,
        List<String> activityAreas,
        List<Project> projects,
        List<Skill> skills,
        Studio studio,
        List<Portfolio> portfolios,
        String userNo
) {
}
