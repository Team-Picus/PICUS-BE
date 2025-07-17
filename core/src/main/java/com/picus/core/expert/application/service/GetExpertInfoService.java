package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.GetExpertInfoQuery;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetExpertInfoService implements GetExpertInfoQuery {

    private final LoadExpertPort loadExpertPort;

    @Override
    public Expert getExpertInfo(String expertNo) {
        // expertNo로 Expert 조회
        return loadExpertPort.findById(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }
}
