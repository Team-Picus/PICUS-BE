package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.adapter.in.web.data.response.RequestApprovalWebResponse;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import org.springframework.stereotype.Component;

@Component
public class RequestApprovalWebMapper {

    public Expert toDomain(RequestApprovalWebRequest webRequest) {
        return Expert.builder()
                .activityCareer(webRequest.activityCareer())
                .activityAreas(webRequest.activityAreas())
                .activityCount(0)
                .approvalStatus(ApprovalStatus.PENDING)
                .projects(webRequest.projects())
                .skills(webRequest.skills())
                .studio(webRequest.studio())
                .portfolios(webRequest.portfolios())
                .build();
    }

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
