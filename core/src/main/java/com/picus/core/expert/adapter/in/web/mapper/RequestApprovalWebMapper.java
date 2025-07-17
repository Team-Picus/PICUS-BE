package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.adapter.in.web.data.response.RequestApprovalWebResponse;
import com.picus.core.expert.application.port.in.command.RequestApprovalCommand;
import com.picus.core.expert.domain.model.Expert;
import org.springframework.stereotype.Component;

@Component
public class RequestApprovalWebMapper {

    public RequestApprovalCommand toCommand(RequestApprovalWebRequest webRequest, String userNo) {
        return RequestApprovalCommand.builder()
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
