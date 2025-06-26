package com.picus.core.old.domain.studio.domain.service;

import com.picus.core.old.domain.studio.application.dto.request.StudioReq;
import com.picus.core.old.domain.studio.domain.entity.Studio;
import com.picus.core.old.domain.studio.domain.repository.StudioRepository;
import com.picus.core.old.global.common.exception.RestApiException;
import com.picus.core.old.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    public Studio findByExpertNo(Long expertNo) {
        return studioRepository.findByExpertNo(expertNo)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }
}
