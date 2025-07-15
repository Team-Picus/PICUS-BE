package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.GetExpertQuery;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
public class GetExpertService implements GetExpertQuery {

    private final LoadExpertPort loadExpertPort;

    @Override
    public Expert getExpertInfo(String expertNo) {
        // expertNo로 Expert 조회
        return loadExpertPort.loadExpertByExpertNo(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }
}
