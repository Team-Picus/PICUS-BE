package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.RequestApprovalUseCase;
import com.picus.core.expert.application.port.in.command.RequestApprovalRequest;
import com.picus.core.expert.application.port.in.mapper.RequestApprovalAppMapper;
import com.picus.core.expert.application.port.out.CreateExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.UserCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@UseCase
@Transactional
public class RequestApprovalService implements RequestApprovalUseCase {

    private final CreateExpertPort createExpertPort;
    private final UserCommandPort userCommandPort;
    private final RequestApprovalAppMapper appMapper;

    /**
     * 전문가 승인 요청
     * ApprovalStatus가 Pending인 Expert 저장
     */
    @Override
    public void requestApproval(RequestApprovalRequest command) {
        Expert expert = appMapper.toDomain(command); // command -> domain

        Expert savedExpert = createExpertPort.saveExpert(expert, command.userNo());// Expert 저장

        userCommandPort.assignExpertNo(command.userNo(), savedExpert.getExpertNo());// User expertNo 할당
    }


}
