package com.picus.core.expert.adapter.in.web.data.request;

import com.picus.core.expert.domain.vo.SkillType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record RequestApprovalWebRequest(
        @NotBlank String activityCareer,
        @NotNull List<@NotBlank String> activityAreas,
        @Valid List<ProjectWebRequest> projects,
        @Valid @NotNull List<SkillWebRequest> skills, // 스킬은 적어도 1개는 무조건 있어야 함
        @Valid StudioWebRequest studio,
        List<String> portfolioLinks) {

    @Builder
    public record ProjectWebRequest(
            @NotBlank String projectName,
            @NotNull LocalDateTime startDate,
            @NotNull LocalDateTime endDate
    ) {
    }

    @Builder
    public record SkillWebRequest(
            @NotNull SkillType skillType,
            @NotBlank String content
    ) {
    }

    @Builder
    public record StudioWebRequest(
            @NotBlank String studioName,
            @NotNull Integer employeesCount,
            @NotBlank String businessHours,
            @NotBlank String address
    ) {
    }
}
