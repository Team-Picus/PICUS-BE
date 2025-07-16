package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.RequestApprovalUseCase;
import com.picus.core.expert.application.port.in.command.RequestApprovalCommand;
import com.picus.core.expert.application.port.in.mapper.RequestApprovalAppMapper;
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
    private final RequestApprovalAppMapper appMapper;

    /**
     * 전문가 승인 요청
     * ApprovalStatus가 Pending인 Expert 저장
     */
    @Override
    public void requestApproval(RequestApprovalCommand command) {
        Expert expert = appMapper.toDomain(command); // command -> domain
        saveExpertPort.saveExpert(expert, command.userNo());
    }


}
