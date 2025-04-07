package com.picus.core.domain.post.application.usecase;

import com.picus.core.domain.post.application.converter.PostConverter;
import com.picus.core.domain.post.application.dto.request.AdditionalOptionCreate;
import com.picus.core.domain.post.application.dto.request.AdditionalOptionUpdate;
import com.picus.core.domain.post.application.dto.request.PostInitial;
import com.picus.core.domain.post.application.dto.request.PostUpdate;
import com.picus.core.domain.post.application.dto.response.PostDetailResponse;
import com.picus.core.domain.post.application.dto.response.PostSummaryResponse;
import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.domain.post.domain.entity.PostStatus;
import com.picus.core.domain.post.domain.service.PostService;
import com.picus.core.domain.shared.area.District;
import com.picus.core.domain.shared.category.Category;
import com.picus.core.domain.shared.category.CategoryType;
import com.picus.core.domain.shared.image.application.dto.response.ImageUrl;
import com.picus.core.domain.shared.image.application.usecase.ImageUseCase;
import com.picus.core.domain.studio.domain.entity.Studio;
import com.picus.core.domain.studio.domain.service.StudioService;
import com.picus.core.global.utils.regex.BadWordFilterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostManagementUseCase {

    private final PostService postService;
    private final StudioService studioService;
    private final ViewTrackerUseCase viewTrackerUseCase;
    private final ImageUseCase imageUseCase;
    private final AdditionalOptionUseCase additionalOptionUseCase;
    private final BadWordFilterUtil badWordFilterUtil;

    /**
     * Post 최초 생성
     * @param expertNo
     * @return
     */
    @Transactional
    public PostSummaryResponse createPost(Long expertNo) {
        Long studioIdByExpertNo = studioService.findByExpertNo(expertNo).getId();
        Post post = postService.create(studioIdByExpertNo);

        return PostConverter.convertSummary(post);
    }

    /**
     * Post 추가 등록
     * @param expertNo
     * @param postInitial
     * @return
     */
    @Transactional
    public PostDetailResponse registerPost(Long expertNo, PostInitial postInitial) {
        // 1. Post 조회
        Post post = postService.findById(postInitial.postId());
        Studio studio = studioService.findByExpertNo(expertNo);

        // 2. Post 의 스튜디오 일치 여부 확인
        if (!post.getStudioNo().equals(studio.getId())) {
            throw new IllegalArgumentException("해당 포스트의 스튜디오와 일치하지 않습니다. postNo: " + post.getId());
        }

        filterBadWords(postInitial.title(), postInitial.detail());

        // 3. Post 초기화
        postService.registerPost(post.getId(),
                postInitial.title(),
                postInitial.detail(),
                postInitial.basicPrice());

        // 4. 추가 옵션 등록
        for (AdditionalOptionCreate additionalOption : postInitial.additionalOptions()) {
            postService.addAdditionalOption(post.getId(), additionalOption);
        }

        // 5. 카테고리 등록
        validateCategories(postInitial.categories());
        for (Category category : postInitial.categories()) {
            postService.addCategory(post.getId(), category);
        }
        studio.updateCategory(postInitial.categories());

        // 6. 가용 지역 등록
        for (District district : postInitial.availableAreas()) {
            postService.addAvailableArea(post.getId(), district);
        }

        // 7. 이미지 조회
        List<ImageUrl> postImages = imageUseCase.findPostImages(post.getId());
        return PostConverter.convertDetail(post, postImages);
    }

    @Transactional
    public PostDetailResponse updatePost(PostUpdate postUpdate) {
        // 1. Post 조회
        Post post = postService.findPostByIdWithDetails(postUpdate.postId());

        filterBadWords(postUpdate.title(), postUpdate.detail());

        // 2. Post 초기화
        postService.updatePost(post.getId(),
                postUpdate.title(),
                postUpdate.detail(),
                postUpdate.basicPrice());

        // 3. 카테고리 등록
        if (postUpdate.categories() != null) {
            validateCategories(postUpdate.categories());
            postService.clearCategory(post.getId());
            for (Category category : postUpdate.categories()) {
                postService.addCategory(post.getId(), category);
            }
        }

        // 4. 가용 지역 등록
        if (postUpdate.availableAreas() != null) {
            postService.clearAvailableArea(post.getId());
            for (District district : postUpdate.availableAreas()) {
                postService.addAvailableArea(post.getId(), district);
            }
        }

        // 5. 추가 옵션 등록
        if (postUpdate.additionalOptionsToAdd() != null) {
            for (AdditionalOptionCreate additionalOption : postUpdate.additionalOptionsToAdd()) {
                additionalOptionUseCase.addAdditionalOption(post.getId(), additionalOption);
            }
        }

        // 6. 추가 옵션 수정
        if (postUpdate.additionalOptionsToUpdate() != null) {
            for (AdditionalOptionUpdate additionalOption : postUpdate.additionalOptionsToUpdate()) {
                additionalOptionUseCase.updateAdditionalOption(post.getId(), additionalOption);
            }
        }

        // 7. 추가 옵션 삭제
        if (postUpdate.additionalOptionsToRemove() != null) {
            for (Long additionalOptionId : postUpdate.additionalOptionsToRemove()) {
                additionalOptionUseCase.removeAdditionalOption(post.getId(), additionalOptionId);
            }
        }


        // 8. 이미지 조회
        List<ImageUrl> postImages = imageUseCase.findPostImages(post.getId());
        return PostConverter.convertDetail(post, postImages);

    }

    /**
     * Post 상세 조회 (옵션 정보 및 상세 정보)
     * @param postId
     * @param isNewView
     * @return
     */
    public PostDetailResponse findPostDetail(Long postId, boolean isNewView) {
        // 1. post 조회
        Post post = postService.findPostByIdWithDetails(postId);

        // 2. [Optional] 조회수 증가
        if (isNewView) {
            viewTrackerUseCase.incrementViewCount(postId);
        }

        PostStatus postStatus = post.getPostStatus();

        // 3. Publish 상태만 조회 가능
        if (postStatus != PostStatus.PUBLISHED) {
            log.error("해당 포스트는 공개되지 않았습니다. postId: {}, status: {}", postId, postStatus);
            throw new IllegalArgumentException("해당 포스트는 공개되지 않았습니다." );
        }

        // 4. 이미지 조회
        List<ImageUrl> postImages = imageUseCase.findPostImages(postId);

        return PostConverter.convertDetail(post, postImages);
    }

    /**
     * 카테고리 검증
     * @param postCategories
     */
    private void validateCategories(Set<Category> postCategories) {
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

    private void filterBadWords(String title, String detail) {
        badWordFilterUtil.filterBadWord(title);
        badWordFilterUtil.filterBadWord(detail);
    }
}
