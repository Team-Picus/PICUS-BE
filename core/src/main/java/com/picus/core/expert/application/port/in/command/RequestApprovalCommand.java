package com.picus.core.expert.application.port.in.command;

import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import lombok.Builder;

import java.util.List;

@Builder
public record RequestApprovalCommand(
        java.lang.String activityCareer,
        List<java.lang.String> activityAreas,
        List<Project> projects,
        List<Skill> skills,
        Studio studio,
        List<String> portfolioLinks,
        java.lang.String userNo
) {
}
