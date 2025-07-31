package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.LoadGalleryUseCase;
import com.picus.core.post.application.port.in.result.LoadGalleryResult;
import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.shared.annotation.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class LoadGalleryService implements LoadGalleryUseCase {

    private final PostReadPort postReadPort;

    @Override
    public Optional<LoadGalleryResult> load(String expertNo) {
        // 해당 전문가의 고정처리된 게시물 조회
        Optional<Post> optionalPost = postReadPort.findByExpertNoAndIsPinnedTrue(expertNo);

        if (optionalPost.isPresent()) { // 고정처리한 게시물이 존재한다면
            Post post = optionalPost.get();
            List<PostImage> postImages = post.getPostImages();

            // TODO: file key -> url 변환 로직
            List<String> imageUrls = List.of("");

            // LoadGalleryResult 매핑
            List<LoadGalleryResult.PostImageResult> postImageResults = postImages.stream()
                    .map(postImage -> LoadGalleryResult.PostImageResult.builder()
                            .imageNo(postImage.getPostImageNo())
                            .fileKey(postImage.getFileKey())
                            .imageUrl("") // 변환 로직 후 기입
                            .imageOrder(postImage.getImageOrder())
                            .build()
                    ).toList();
            return Optional.ofNullable(LoadGalleryResult.builder()
                    .postNo(post.getPostNo())
                    .images(postImageResults)
                    .title(post.getTitle())
                    .oneLineDescription(post.getOneLineDescription())
                    .build());

        } else { // 고정처리한 게시물이 존재하지 않는다면 (아직 게시물이 없거나 설정 안했을 수도 있음)
            return Optional.empty();
        }
    }
}
