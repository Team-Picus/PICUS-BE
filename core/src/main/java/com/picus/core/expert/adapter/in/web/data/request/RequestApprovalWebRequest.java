package com.picus.core.expert.adapter.in.web.data.request;

import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import com.picus.core.expert.domain.vo.Portfolio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

// TODO: Project, Skill, Studio, Portfolio를 별도의 DTO 객체로 변경
public record RequestApprovalWebRequest(
        @NotBlank String activityCareer,
        @NotNull List<String> activityAreas,
        List<Project> projects,
        List<Skill> skills,
        Studio studio,
        List<Portfolio> portfolios) {
}
