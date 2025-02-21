package com.picus.core.domain.post.service;

import com.picus.core.domain.post.entity.view.ViewCount;
import com.picus.core.domain.post.exception.PostNotFoundException;
import com.picus.core.domain.post.repository.view.primary.ViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewTrackerService {

    private final ViewCountRepository viewCountRepository;

    public Integer findViewCount(Long postId) {
        return viewCountRepository.findViewCount(ViewCount.generateKey(postId))
                .orElseThrow(PostNotFoundException::new);
    }

    private void incrementViewCount(Long postId) {
        viewCountRepository.increment(ViewCount.generateKey(postId));
    }
}
