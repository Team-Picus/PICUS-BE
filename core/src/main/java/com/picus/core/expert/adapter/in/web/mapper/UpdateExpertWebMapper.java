package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.request.ExpertBasicInfoCommandWebRequest;
import com.picus.core.expert.application.port.in.command.ExpertBasicInfoCommandRequest;
import org.springframework.stereotype.Component;

@Component
public class UpdateExpertWebMapper {

    public ExpertBasicInfoCommandRequest toBasicInfoAppRequest(ExpertBasicInfoCommandWebRequest webRequest, String currentUserNo) {
        return ExpertBasicInfoCommandRequest.builder()
                .currentUserNo(currentUserNo)
                .profileImageFileKey(webRequest.profileImageFileKey())
                .backgroundImageFileKey(webRequest.backgroundImageFileKey())
                .nickname(webRequest.nickname())
                .link(webRequest.link())
                .intro(webRequest.intro())
                .build();
    }
}
