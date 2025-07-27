package com.picus.core.expert.application.port.in.request;

import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import com.picus.core.expert.domain.vo.Portfolio;
import lombok.Builder;

import java.util.List;

@Builder
public record RequestApprovalAppReq(
        String activityCareer,
        List<String> activityAreas,
        List<Project> projects,
        List<Skill> skills,
        Studio studio,
        List<Portfolio> portfolios,
        String userNo
) {
}
