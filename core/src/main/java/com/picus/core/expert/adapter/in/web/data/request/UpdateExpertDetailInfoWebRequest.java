package com.picus.core.expert.adapter.in.web.data.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UpdateExpertDetailInfoWebRequest(
        String activityCareer,
        @NotNull List<String> activityAreas,
        List<ProjectWebRequest> projects,
        List<SkillWebRequest> skills,
        StudioWebRequest studio
) {
    @Builder
    public record ProjectWebRequest(
            String projectNo,
            String projectName,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
    }
    @Builder
    public record SkillWebRequest(
            String skillNo,
            String skillType,
            String content
    ) {
    }
    @Builder
    public record StudioWebRequest(
            String studioNo,
            String studioName,
            Integer employeesCount,
            String businessHours,
            String address
    ) {
    }
}
