package com.picus.core.domain.post.application.usecase;

import com.picus.core.domain.post.application.converter.PostConverter;
import com.picus.core.domain.post.application.dto.request.AdditionalOptionDto;
import com.picus.core.domain.post.application.dto.request.PostInitialDto;
import com.picus.core.domain.post.application.dto.response.PostDetailDto;
import com.picus.core.domain.post.application.dto.response.PostSummaryDto;
import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.domain.post.domain.entity.PostStatus;
import com.picus.core.domain.post.domain.service.PostService;
import com.picus.core.domain.studio.application.usecase.StudioUseCase;
import com.picus.core.global.common.area.entity.District;
import com.picus.core.global.common.category.entity.Category;
import com.picus.core.global.common.category.entity.CategoryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostUseCase {

    private final PostService postService;
    private final StudioUseCase studioUseCase;

    @Transactional
    public PostSummaryDto createPost(Long expertNo) {
        Long studioIdByExpertNo = studioUseCase.findStudioIdByExpertNo(expertNo);
        Post post = postService.create(studioIdByExpertNo);

        return PostConverter.convertSummary(post);
    }

    @Transactional
    public PostDetailDto registerPost(Long expertNo, PostInitialDto postInitialDto) {
        // 1. Post 조회
        Post post = postService.findById(postInitialDto.postId());
        Long studioNo = studioUseCase.findStudioIdByExpertNo(expertNo);

        // 2. Post 의 스튜디오 일치 여부 확인
        if (!post.getStudioNo().equals(studioNo)) {
            throw new IllegalArgumentException("해당 포스트의 스튜디오와 일치하지 않습니다. postNo: " + post.getId());
        }

        // 3. Post 초기화
        postService.registerPost(post.getId(),
                postInitialDto.title(),
                postInitialDto.detail(),
                postInitialDto.basicPrice());

        // 4. 추가 옵션 등록
        for (AdditionalOptionDto additionalOption : postInitialDto.additionalOptions()) {
            postService.addAdditionalOption(post.getId(), additionalOption);
        }

//        // 5. 이미지 등록
//        for (String imageKey : postInitialDto.imageKeys()) {
//            postService.addImages(post.getId(), imageKey);
//        }

        // 6. 카테고리 등록
        validateCategories(postInitialDto.categories());
        for (Category category : postInitialDto.categories()) {
            postService.addCategory(post.getId(), category);
        }

        // 7. 가용 지역 등록
        for (District district : postInitialDto.availableAreas()) {
            postService.addAvailableArea(post.getId(), district);
        }

        return PostConverter.convertDetail(post);
    }

    public PostDetailDto findPostDetail(Long userId, Long postId) {
        Post post = postService.findById(postId);

        // TODO 조회수 1회 증가 추가해야함.

        PostStatus postStatus = post.getPostStatus();

        // Publish 상태만 조회 가능
        if (postStatus != PostStatus.PUBLISHED) {
            log.error("해당 포스트는 공개되지 않았습니다. postId: {}, status: {}", postId, postStatus);
            throw new IllegalArgumentException("해당 포스트는 공개되지 않았습니다." );
        }

        return PostConverter.convertDetail(post);
    }

    private void validateCategories(List<Category> postCategories) {
        boolean hasLocation = postCategories.stream()
                .anyMatch(pc -> pc.getType() == CategoryType.LOCATION);
        boolean hasTheme = postCategories.stream()
                .anyMatch(pc -> pc.getType() == CategoryType.THEME);
        boolean hasMood = postCategories.stream()
                .anyMatch(pc -> pc.getType() == CategoryType.MOOD);

        if (!hasLocation || !hasTheme || !hasMood) {
            throw new IllegalStateException("각 카테고리(장소, 테마, 분위기)는 최소 1개 이상 있어야 합니다.");
        }
    }

}
