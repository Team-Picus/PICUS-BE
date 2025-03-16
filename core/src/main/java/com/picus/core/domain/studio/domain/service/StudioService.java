package com.picus.core.domain.studio.domain.service;

import com.picus.core.domain.studio.domain.entity.Studio;
import com.picus.core.domain.studio.domain.repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StudioService {
    private final StudioRepository studioRepository;

    public Studio findByExpertNo(Long expertNo) {
        return studioRepository.findByExpertNo(expertNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 스튜디오를 찾을 수 없습니다. expertNo: " + expertNo));
    }
}
