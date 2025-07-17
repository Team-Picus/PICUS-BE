package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.GetExpertBasicInfoWebResponse;
import com.picus.core.expert.adapter.in.web.data.response.GetExpertDetailInfoWebResponse;
import com.picus.core.expert.application.port.in.response.GetExpertBasicInfoAppResponse;
import com.picus.core.expert.domain.model.Expert;
import org.springframework.stereotype.Component;

@Component
public class GetExpertWebMapper {

    public GetExpertBasicInfoWebResponse toBasicInfo(GetExpertBasicInfoAppResponse appResponse) {
        return GetExpertBasicInfoWebResponse.builder()
                .activityDuration(appResponse.activityDuration())
                .activityCount(appResponse.activityCount())
                .lastActivityAt(appResponse.lastActivityAt())
                .intro(appResponse.intro())
                .backgroundImageUrl(appResponse.backgroundImageUrl())
                .nickname(appResponse.nickname())
                .profileImageUrl(appResponse.profileImageUrl())
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
