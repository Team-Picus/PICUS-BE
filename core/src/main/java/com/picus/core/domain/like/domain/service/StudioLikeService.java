package com.picus.core.domain.like.domain.service;

import com.picus.core.domain.like.domain.entity.studio.StudioLike;
import com.picus.core.domain.like.domain.repository.StudioLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudioLikeService {

    private final StudioLikeRepository studioLikeRepository;

    public StudioLike save(Long userNo, Long studioNo) {
        return studioLikeRepository.save(new StudioLike(userNo, studioNo));
    }

    public void delete(Long userNo, Long studioNo) {
        studioLikeRepository.delete(userNo, studioNo);
    }
}
