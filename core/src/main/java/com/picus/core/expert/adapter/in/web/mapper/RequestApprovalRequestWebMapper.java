package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.application.port.in.RequestApprovalRequest;
import com.picus.core.expert.adapter.in.web.data.request.RequestApprovalWebRequest;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import org.springframework.stereotype.Component;

// Mapper 입력/출력 하나로 합치기
@Component
public class RequestApprovalRequestWebMapper {

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
}
