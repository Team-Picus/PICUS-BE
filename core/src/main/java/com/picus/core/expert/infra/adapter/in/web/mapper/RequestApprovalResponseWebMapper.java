package com.picus.core.expert.infra.adapter.in.web.mapper;

import com.picus.core.expert.application.port.in.RequestApprovalRequest;
import com.picus.core.expert.application.port.in.RequestApprovalResponse;
import com.picus.core.expert.infra.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.infra.adapter.in.web.data.response.RequestApprovalWebResponse;
import org.springframework.stereotype.Component;

@Component
public class RequestApprovalResponseWebMapper {

    public RequestApprovalWebResponse toWebResponse(RequestApprovalResponse applicationResponse) {
        return RequestApprovalWebResponse
                .builder()
                .expertNo(applicationResponse.expertNo())
                .activityCareer(applicationResponse.activityCareer())
                .projects(applicationResponse.projects())
                .skills(applicationResponse.skills())
                .studio(applicationResponse.studio())
                .portfolios(applicationResponse.portfolios())
                .build();
    }
}
