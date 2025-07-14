package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.RejectRequestUseCase;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.model.Expert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RejectRequestService implements RejectRequestUseCase {
    private final LoadExpertPort loadExpertPort;
    private final UpdateExpertPort updateExpertPort;

    @Override
    public void rejectRequest(String expertNo) {
        // 받은 expertNo로 expert를 가져옴
        Expert expert = loadExpertPort.loadExpertByExpertNo(expertNo)
                .orElseThrow(RuntimeException::new);// TODO: 예외 클래스 정의

        // 해당 expert의 approval status를 Approval로 변경함
        expert.rejectApprovalRequest();

        // 변경된 expert를 데이터베이스에 반영함
        updateExpertPort.updateExpert(expert);
    }
}
