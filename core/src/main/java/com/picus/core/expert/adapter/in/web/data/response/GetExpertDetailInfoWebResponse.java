package com.picus.core.expert.adapter.in.web.data.response;

import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import lombok.Builder;

import java.util.List;

@Builder
public record GetExpertDetailInfoWebResponse(
        String activityCareer,
        List<Project> projects,
        List<Skill> skills,
        List<String> activityAreas,
        Studio studio
) {
}
