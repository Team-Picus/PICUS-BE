package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.GetExpertBasicInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse;
import com.picus.core.expert.domain.model.Expert;
import org.springframework.stereotype.Component;

@Component
public class GetExpertWebMapper {

    public GetExpertBasicInfoWebResponse toBasicInfo(Expert expert) {
        return GetExpertBasicInfoWebResponse.builder()
                .activityDuration(expert.getActivityDuration())
                .activityCount(expert.getActivityCount())
                .lastActivityAt(expert.getLastActivityAt())
                .intro(expert.getIntro())
                .backgroundImageUrl(expert.getBackgroundImageUrl())
                .build();
    }

    public GetExpertDetailInfoWebResponse toDetailInfo(Expert expert) {
        return GetExpertDetailInfoWebResponse.builder()
                .activityCareer(expert.getActivityCareer())
                .projects(expert.getProjects())
                .skills(expert.getSkills())
                .activityAreas(expert.getActivityAreas())
                .studio(expert.getStudio())
                .build();
    }
}
