package com.picus.core.domain.studio.domain.service;

import com.picus.core.domain.studio.application.dto.request.StudioReq;
import com.picus.core.domain.studio.domain.entity.Studio;
import com.picus.core.domain.studio.domain.repository.StudioRepository;
import com.picus.core.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StudioService {

    private final StudioRepository studioRepository;

    public Studio save(Long expertNo, StudioReq request) {
        return studioRepository.save(Studio.create(expertNo, request));
    }

    public boolean isExist(Long expertNo) {
        return studioRepository.existsByExpertNo(expertNo);
    }

    public Studio findStudio(Long studioNo) {
        return studioRepository.findById(studioNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }
}
