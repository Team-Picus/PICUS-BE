package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.RejectRequestUseCase;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@RequiredArgsConstructor
@UseCase
public class RejectRequestService implements RejectRequestUseCase {
    private final LoadExpertPort loadExpertPort;
    private final UpdateExpertPort updateExpertPort;

    @Override
    public void rejectRequest(String expertNo) {
        // 받은 expertNo로 expert를 가져옴
        Expert expert = loadExpertPort.loadExpertByExpertNo(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // 해당 expert의 approval status를 Approval로 변경함
        expert.rejectApprovalRequest();

        // 변경된 expert를 데이터베이스에 반영함
        updateExpertPort.updateExpert(expert);
    }
}
