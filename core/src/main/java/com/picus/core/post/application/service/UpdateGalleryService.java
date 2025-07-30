package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.UpdateGalleryUseCase;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.application.port.out.PostUpdatePort;
import com.picus.core.post.domain.Post;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._FORBIDDEN;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional
public class UpdateGalleryService implements UpdateGalleryUseCase {

    private final UserQueryPort userQueryPort;

    private final PostReadPort postReadPort;
    private final PostUpdatePort postUpdatePort;

    @Override
    public void update(String postNo, String currentUserNo) {
        // 1. 현재 사용자의 expertNo 조회
        String expertNo = getCurrentExpertNo(currentUserNo);

        // 2. 기존 고정 처리된 게시물 고정해제
        postReadPort.findByExpertNoAndIsPinnedTrue(expertNo)
                .ifPresent(post -> {
                    // 고정해제
                    post.unpin();
                    // 데이터베이스 반영
                    postUpdatePort.update(post);
                });

        // 3. 요청 Post 고정처리
        Post post = postReadPort.findById(postNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        // Post 고정처리
        post.pin();
        // 데이터베이스 반영
        postUpdatePort.update(post);
    }

    private String getCurrentExpertNo(String userNo) {
        User user = userQueryPort.findById(userNo);
        return Optional.ofNullable(user.getExpertNo())
                .orElseThrow(() -> new RestApiException(_FORBIDDEN));
    }

}
