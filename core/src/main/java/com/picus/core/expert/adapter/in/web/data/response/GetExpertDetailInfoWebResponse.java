package com.picus.core.expert.adapter.in.web.data.response;

import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.SkillType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GetExpertDetailInfoWebResponse(
        String activityCareer,
        List<ProjectWebResponse> projects,
        List<SkillWebResponse> skills,
        List<String> activityAreas,
        StudioWebResponse studio
) {
    @Builder
    public record ProjectWebResponse(
            String projectNo,
            String projectName,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {}


    @Builder
    public record SkillWebResponse(
            String skillNo,
            SkillType skillType,
            String content
    ) {}

    @Builder
    public record StudioWebResponse(
            String studioNo,
            String studioName,
            Integer employeesCount,
            String businessHours,
            String address
    ) {}
}
