package com.picus.core.domain.like.application;

import com.picus.core.domain.like.domain.service.PostLikeService;
import com.picus.core.domain.like.domain.service.StudioLikeService;
import com.picus.core.domain.studio.domain.entity.Studio;
import com.picus.core.domain.studio.domain.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Retryable(
        value = { OptimisticLockingFailureException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
)
@RequiredArgsConstructor
public class LikeManagementUseCase {

    private final PostLikeService postLikeService;
    private final StudioService studioService;
    private final StudioLikeService studioLikeService;

    public void likePost(Long userNo, Long postNo) {
        postLikeService.save(userNo, postNo);
        // Post post = postService.findPost(postNo);
        // post.updateLikeCount(1);
    }

    public void unlikePost(Long userNo, Long postNo) {
        postLikeService.delete(userNo, postNo);
        // Post post = postService.findPost(postNo);
        // post.updateLikeCount(-1);
    }

    public void likeStudio(Long userNo, Long studioNo) {
        studioLikeService.save(userNo, studioNo);
        Studio studio = studioService.findStudio(studioNo);
        studio.updateLikeCount(1);
    }

    public void unlikeStudio(Long userNo, Long studioNo) {
        studioLikeService.delete(userNo, studioNo);
        Studio studio = studioService.findStudio(studioNo);
    }
}
