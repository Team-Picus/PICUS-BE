package com.picus.core.expert.adapter.in.web.data.response;

import com.picus.core.expert.domain.vo.SkillType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoadExpertDetailInfoResponse(
        String activityCareer,
        List<ProjectResponse> projects,
        List<SkillResponse> skills,
        List<String> activityAreas,
        StudioResponse studio
) {
    @Builder
    public record ProjectResponse(
            String projectNo,
            String projectName,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {}


    @Builder
    public record SkillResponse(
            String skillNo,
            SkillType skillType,
            String content
    ) {}

    @Builder
    public record StudioResponse(
            String studioNo,
            String studioName,
            Integer employeesCount,
            String businessHours,
            String address
    ) {}
}
