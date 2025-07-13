package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.application.port.in.RequestApprovalResponse;
import com.picus.core.expert.adapter.in.web.data.response.RequestApprovalWebResponse;
import com.picus.core.expert.domain.model.Expert;
import org.springframework.stereotype.Component;

@Component
public class RequestApprovalResponseWebMapper {

    public RequestApprovalWebResponse toWebResponse(Expert domain) {
        return RequestApprovalWebResponse.builder()
                .expertNo(domain.getExpertNo())
                .activityCareer(domain.getActivityCareer())
                .projects(domain.getProjects())
                .activityAreas(domain.getActivityAreas())
                .skills(domain.getSkills())
                .studio(domain.getStudio())
                .portfolios(domain.getPortfolios())
                .build();
    }
}
