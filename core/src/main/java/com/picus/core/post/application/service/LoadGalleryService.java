package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.LoadGalleryUseCase;
import com.picus.core.post.application.port.in.mapper.LoadGalleryCommandMapper;
import com.picus.core.post.application.port.in.result.LoadGalleryResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class LoadGalleryService implements LoadGalleryUseCase {

    private final PostReadPort postReadPort;

    private final LoadGalleryCommandMapper appMapper;

    @Override
    public Optional<LoadGalleryResult> load(String expertNo) {
        // 해당 전문가의 고정처리된 게시물 조회
        Optional<Post> post = postReadPort.findByExpertNoAndIsPinnedTrue(expertNo);

        if(post.isPresent()) { // 고정처리한 게시물이 존재한다면
            // 이미지 순서가 1인 이미지가 썸네일
            PostImage thumbnail = post.get().getPostImages().stream()
                    .filter(postImage -> postImage.getImageOrder() == 1)
                    .findFirst()
                    .orElseThrow(() -> new RestApiException(GlobalErrorStatus._INTERNAL_SERVER_ERROR));

            // TODO: file key -> url 변환 로직

            // 매퍼를 통해 LoadGalleryResult 매핑
            return Optional.ofNullable(appMapper.toAppResp(post.get(), ""));

        } else { // 고정처리한 게시물이 존재하지 않는다면 (아직 게시물이 없거나 설정 안했을 수도 있음)
            return Optional.empty();
        }
    }
}
