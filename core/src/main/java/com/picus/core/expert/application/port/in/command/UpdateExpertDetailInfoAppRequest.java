package com.picus.core.expert.application.port.in.command;

import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateExpertDetailInfoAppRequest(
        String currentUserNo,
        String activityCareer,
        List<String> activityAreas,
        List<ProjectCommand> projects,
        List<SkillCommand> skills,
        StudioCommand studio
) {
}
