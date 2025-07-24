package com.picus.core.expert.application.port.in.request;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateExpertDetailInfoAppReq(
        String currentUserNo,
        String activityCareer,
        List<String> activityAreas,
        List<UpdateProjectAppReq> projects,
        List<UpdateSkillAppReq> skills,
        UpdateStudioAppReq studio
) {
    public UpdateExpertDetailInfoAppReq {
        activityAreas = activityAreas != null ? activityAreas : List.of();
        projects = projects != null ? projects : List.of();
        skills = skills != null ? skills : List.of();
    }
}
