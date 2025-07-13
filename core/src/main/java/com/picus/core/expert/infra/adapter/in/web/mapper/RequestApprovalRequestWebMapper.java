package com.picus.core.expert.infra.adapter.in.web.mapper;

import com.picus.core.expert.application.port.in.RequestApprovalRequest;
import com.picus.core.expert.infra.adapter.in.web.data.request.RequestApprovalWebRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestApprovalRequestWebMapper {

    public RequestApprovalRequest toApplicationRequest(RequestApprovalWebRequest webRequest) {
        return RequestApprovalRequest.builder()
                .activityCareer(webRequest.activityCareer())
                .activityAreas(webRequest.activityAreas())
                .projects(webRequest.projects())
                .skills(webRequest.skills())
                .studio(webRequest.studio())
                .portfolios(webRequest.portfolios())
                .build();
    }
}
