package com.picus.core.expert.application.port.in.request;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateExpertDetailInfoCommand(
        String currentUserNo,
        String activityCareer,
        List<String> activityAreas,
        List<UpdateProjectCommand> projects,
        List<UpdateSkillCommand> skills,
        UpdateStudioCommand studio
) {
    public UpdateExpertDetailInfoCommand {
        activityAreas = activityAreas != null ? activityAreas : List.of();
        projects = projects != null ? projects : List.of();
        skills = skills != null ? skills : List.of();
    }
}
