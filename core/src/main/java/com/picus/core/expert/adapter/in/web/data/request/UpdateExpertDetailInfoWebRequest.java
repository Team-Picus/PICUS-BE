package com.picus.core.expert.adapter.in.web.data.request;

import com.picus.core.expert.application.port.in.command.ChangeStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
public record UpdateExpertDetailInfoWebRequest(
        String activityCareer,
        @NotNull List<String> activityAreas, // 항상 기존값과 함께 넘긴다.
        @Valid List<ProjectWebRequest> projects,
        @Valid List<SkillWebRequest> skills,
        @Valid StudioWebRequest studio
) {
    @Builder
    public record ProjectWebRequest(
            String projectNo,
            String projectName,
            LocalDateTime startDate,
            LocalDateTime endDate,
            @NotNull ChangeStatus changeStatus
    ) {
    }
    @Builder
    public record SkillWebRequest(
            String skillNo,
            String skillType,
            String content,
            @NotNull ChangeStatus changeStatus
    ) {
    }
    @Builder
    public record StudioWebRequest(
            String studioNo,
            String studioName,
            Integer employeesCount,
            String businessHours,
            String address,
            @NotNull ChangeStatus changeStatus
    ) {
    }
}
