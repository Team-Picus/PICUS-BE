package com.picus.core.domain.expert.domain.service;

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
}
