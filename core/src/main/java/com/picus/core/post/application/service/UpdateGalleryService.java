package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.UpdateGalleryUseCase;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.application.port.out.PostUpdatePort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional
public class UpdateGalleryService implements UpdateGalleryUseCase {

    private final PostReadPort postReadPort;
    private final PostUpdatePort postUpdatePort;

    @Override
    public void update(String postNo, String currentUserNo) {
        // 기존 고정 처리된 게시물 고정해제
        postReadPort.findByExpertNoAndIsPinnedTrue(currentUserNo)
                .ifPresent(post -> {
                    // 고정해제
                    post.unpin();
                    // 데이터베이스 반영
                    postUpdatePort.update(post);
                });

        // 요청 Post 고정처리
        Post post = postReadPort.findById(postNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // Post 고정처리
        post.pin();

        // 데이터베이스 반영
        postUpdatePort.update(post);
    }

}
