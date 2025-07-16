package com.picus.core.expert.application.port.in.command;

import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.Portfolio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record RequestApprovalCommand(
        String activityCareer,
        List<ActivityArea> activityAreas,
        List<Project> projects,
        List<Skill> skills,
        Studio studio,
        List<Portfolio> portfolios,
        String userNo
) {
}
