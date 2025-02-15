package com.picus.core.domain.post.service;

import com.picus.core.domain.post.entity.view.ViewCount;
import com.picus.core.domain.post.entity.view.ViewHistory;
import com.picus.core.domain.post.exception.PostNotFoundException;
import com.picus.core.domain.post.repository.view.primary.ViewCountRepository;
import com.picus.core.domain.post.repository.view.primary.ViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ViewTrackerService {

    private final ViewCountRepository viewCountRepository;
    private final ViewHistoryRepository viewHistoryRepository;

    public Integer findViewCount(Long postId) {
        return viewCountRepository.findViewCount(ViewCount.generateKey(postId))
                .orElseThrow(PostNotFoundException::new);
    }

    @Transactional
    public void updateViewCount(Long userId, Long postId) {
        if(isViewed(userId, postId))
            return;

        incrementViewCount(postId);
        saveViewHistory(userId, postId);
    }

    private Boolean isViewed(Long userId, Long postId) {
        return viewHistoryRepository.existsById(ViewHistory.generateKey(userId, postId));
    }

    private void incrementViewCount(Long postId) {
        viewCountRepository.increment(ViewCount.generateKey(postId));
    }

    private void saveViewHistory(Long userId, Long postId) {
        viewHistoryRepository.save(ViewHistory.generateKey(userId, postId));
    }
}
