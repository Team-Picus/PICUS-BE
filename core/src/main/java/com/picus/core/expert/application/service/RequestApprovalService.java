package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.RequestApprovalUseCase;
import com.picus.core.expert.application.port.in.command.RequestApprovalCommand;
import com.picus.core.expert.application.port.in.mapper.RequestApprovalCommandMapper;
import com.picus.core.expert.application.port.out.ExpertCreatePort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.out.UserUpdatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@UseCase
@Transactional
public class RequestApprovalService implements RequestApprovalUseCase {

    private final ExpertCreatePort expertCreatePort;
    private final UserUpdatePort userUpdatePort;
    private final RequestApprovalCommandMapper appMapper;

    /**
     * 전문가 승인 요청
     * ApprovalStatus가 Pending인 Expert 저장
     */
    @Override
    public void requestApproval(RequestApprovalCommand command) {
        Expert expert = appMapper.toDomain(command); // command -> domain

        Expert savedExpert = expertCreatePort.create(expert, command.userNo());// Expert 저장

        userUpdatePort.assignExpertNo(command.userNo(), savedExpert.getExpertNo());// User expertNo 할당
    }


}
