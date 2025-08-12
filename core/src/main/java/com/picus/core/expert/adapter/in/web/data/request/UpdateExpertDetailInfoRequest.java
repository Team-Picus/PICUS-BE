package com.picus.core.expert.adapter.in.web.data.request;

import com.picus.core.expert.application.port.in.command.ChangeStatus;
import com.picus.core.expert.domain.vo.SkillType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UpdateExpertDetailInfoRequest(
        @NotBlank String activityCareer,
        @NotNull List<String> activityAreas,
        @Valid List<ProjectWebRequest> projects,
        @Valid List<SkillWebRequest> skills,
        @Valid StudioWebRequest studio
) {
    @Builder
    public record ProjectWebRequest(
            String projectNo,
            @NotBlank String projectName,
            @NotNull LocalDateTime startDate,
            @NotNull LocalDateTime endDate,
            @NotNull ChangeStatus changeStatus
    ) {
    }

    @Builder
    public record SkillWebRequest(
            String skillNo,
            @NotNull SkillType skillType,
            @NotBlank String content,
            @NotNull ChangeStatus changeStatus
    ) {
    }

    @Builder
    public record StudioWebRequest(
            String studioNo,
            @NotBlank String studioName,
            @NotNull Integer employeesCount,
            @NotBlank String businessHours,
            @NotBlank String address,
            @NotNull ChangeStatus changeStatus
    ) {
    }
}
