package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.RequestApprovalRequest;
import com.picus.core.expert.application.port.in.RequestApprovalUseCase;
import com.picus.core.expert.application.port.out.SaveExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class RequestApprovalService implements RequestApprovalUseCase {

    private final SaveExpertPort saveExpertPort;

    /**
     * 전문가 승인 요청
     * ApprovalStatus가 Pending인 Expert 저장
     */
    @Override
    public Expert requestApproval(RequestApprovalRequest requestApprovalRequest) {

        Expert expert = createExpert(requestApprovalRequest);

        return saveExpertPort.saveExpert(expert);
    }

    private Expert createExpert(RequestApprovalRequest requestApprovalRequest) {
        return Expert.builder()
                .activityCareer(requestApprovalRequest.activityCareer())
                .approvalStatus(ApprovalStatus.PENDING)
                .projects(requestApprovalRequest.projects())
                .activityAreas(requestApprovalRequest.activityAreas())
                .skills(requestApprovalRequest.skills())
                .studio(requestApprovalRequest.studio())
                .portfolioLinks(requestApprovalRequest.portfolios())
                .build();
    }
}
