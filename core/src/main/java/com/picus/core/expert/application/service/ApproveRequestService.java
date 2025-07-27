package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.ApproveRequestUseCase;
import com.picus.core.expert.application.port.out.ReadExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.shared.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@UseCase
@Transactional
public class ApproveRequestService implements ApproveRequestUseCase {

    private final ReadExpertPort readExpertPort;
    private final UpdateExpertPort updateExpertPort;

    @Override
    public void approveRequest(String expertNo) {

        // 받은 expertNo로 expert를 가져옴
        Expert expert = readExpertPort.findById(expertNo)
                .orElseThrow(RuntimeException::new);// TODO: 예외 클래스 정의

        // 해당 expert의 approval status를 Approval로 변경함
        expert.approveApprovalRequest();

        // 변경된 expert를 데이터베이스에 반영함
        updateExpertPort.update(expert);
    }
}
