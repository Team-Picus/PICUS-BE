package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.application.port.in.command.RequestApprovalRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestApprovalWebMapper {

    public RequestApprovalRequest toCommand(RequestApprovalWebRequest webRequest, String userNo) {
        return RequestApprovalRequest.builder()
                .activityCareer(webRequest.activityCareer())
                .activityAreas(webRequest.activityAreas())
                .projects(webRequest.projects())
                .skills(webRequest.skills())
                .studio(webRequest.studio())
                .portfolios(webRequest.portfolios())
                .userNo(userNo)
                .build();
    }
}
