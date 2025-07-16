package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.RequestApprovalUseCase;
import com.picus.core.expert.application.port.in.command.RequestApprovalCommand;
import com.picus.core.expert.application.port.out.SaveExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.shared.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@UseCase
@Transactional
public class RequestApprovalService implements RequestApprovalUseCase {

    private final SaveExpertPort saveExpertPort;

    /**
     * 전문가 승인 요청
     * ApprovalStatus가 Pending인 Expert 저장
     */
    @Override
    public void requestApproval(RequestApprovalCommand command) {
        Expert expert = createExpert(command); // command -> domain
        saveExpertPort.saveExpert(expert, command.userNo());
    }

    private Expert createExpert(RequestApprovalCommand command) {
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
