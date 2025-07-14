package com.picus.core.expert.adapter.in.web.data.response;

import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.Portfolio;
import lombok.Builder;

import java.util.List;

@Builder
public record RequestApprovalWebResponse(
        String expertNo,
        String activityCareer,
        List<Project> projects,
        List<ActivityArea> activityAreas,
        List<Skill> skills,
        Studio studio,
        List<Portfolio> portfolios
){
}
