package com.picus.core.expert.application.port.in.command;

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
    public UpdateExpertDetailInfoAppRequest {
        activityAreas = activityAreas != null ? activityAreas : List.of();
        projects = projects != null ? projects : List.of();
        skills = skills != null ? skills : List.of();
    }
}
