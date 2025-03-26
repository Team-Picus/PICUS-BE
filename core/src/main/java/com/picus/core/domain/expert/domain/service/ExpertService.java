package com.picus.core.domain.expert.domain.service;

import com.picus.core.domain.expert.application.dto.request.RegExpReq;
import com.picus.core.domain.expert.domain.entity.Expert;
import com.picus.core.domain.expert.domain.repository.ExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpertService {

    private final ExpertRepository expertRepository;

    public boolean isExist(Long expertNo) {
        return expertRepository.existsById(expertNo);
    }

    public Expert save(Long userNo, RegExpReq request) {
        return expertRepository.save(Expert.create(userNo, request));
    }
}
