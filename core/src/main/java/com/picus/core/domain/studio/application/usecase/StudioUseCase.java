package com.picus.core.domain.studio.application.usecase;

import com.picus.core.domain.studio.domain.service.StudioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StudioUseCase {

    private final StudioService studioService;

    public Long findStudioIdByExpertNo(Long expertNo) {
        return studioService.findByExpertNo(expertNo).getId();
    }


}
