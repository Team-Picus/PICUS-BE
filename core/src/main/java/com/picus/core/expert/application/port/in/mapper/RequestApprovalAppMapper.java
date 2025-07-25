package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.command.RequestApprovalRequest;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import org.springframework.stereotype.Component;

@Component
public record RequestApprovalAppMapper() {

    public Expert toDomain(RequestApprovalRequest command) {
        return Expert.builder()
                .activityCareer(command.activityCareer())
                .activityAreas(command.activityAreas())
                .activityCount(0) // 활동수를 0으로 초기화
                .approvalStatus(ApprovalStatus.PENDING) // 승인상태를 PENDING으로 초기화
                .projects(command.projects())
                .skills(command.skills())
                .studio(command.studio())
                .portfolios(command.portfolios())
                .build();
    }
}
