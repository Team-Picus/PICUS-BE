package com.picus.core.domain.post.domain.service;

import com.picus.core.domain.post.application.dto.request.AdditionalOptionCreate;
import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.domain.post.domain.repository.PostRepository;
import com.picus.core.domain.shared.area.entity.District;
import com.picus.core.domain.shared.category.entity.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {
    private final PostRepository postRepository;

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow();
    }

    /**
     * 신규 포스트를 생성하는 서비스 메서드
     *
     * @param studioNo 스튜디오 id
     * @return 생성된 포스트 엔티티
     */
    @Transactional
    public Post create(Long studioNo) {
        return new Post(studioNo);
    }

    /**
     * Post의 기본정보를 저장하는 api
     *
     * @param postId     포스트 ID
     * @param title      포스트 제목
     * @param detail     포스트 상세 내용
     * @param basicPrice 기본 옵션 가격
     * @return 저장된 Post 엔티티
     */
    @Transactional
    public Post registerPost(Long postId,
                             String title,
                             String detail,
                             Integer basicPrice) {
        // 1. Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스튜디오를 찾을 수 없습니다. studioNo: " + postId));

        // 2. Post 초기화 (DRAFT 상태에서 title, detail, availableAreas, 기본 옵션 설정)
        post.register(title, detail, basicPrice);

        // 5. Repository를 통해 Post 저장 (cascade 옵션 덕분에 연관된 엔티티들도 함께 persist)
        return postRepository.save(post);
    }

    @Transactional
    public void addCategory(Long postId, Category category) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다. postId: " + postId));

        post.addPostCategory(category);
    }

    @Transactional
    public void addAvailableArea(Long postId, District district) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다. postId: " + postId));

        post.addAvailableArea(district);
    }

    @Transactional
    public void clearCategory(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다. postId: " + postId));

        post.clearPostCategories();
    }

    @Transactional
    public void clearAvailableArea(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다. postId: " + postId));

        post.clearPostDistricts();
    }
    /**
     * Post -> BasicOption -> AdditionalOption 추가하는 메서드
     * @param postId 포스트 ID
     * @param additionalOptionCreate 추가할 옵션 정보
     */
    @Transactional
    public boolean addAdditionalOption(Long postId, AdditionalOptionCreate additionalOptionCreate) {
        // 1. Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다. postId: " + postId));

        // 2. 추가 옵션 저장
        post.getBasicOption().addAdditionalOption(
                additionalOptionCreate.name(),
                additionalOptionCreate.pricePerUnit(),
                additionalOptionCreate.max(),
                additionalOptionCreate.base(),
                additionalOptionCreate.increment()
        );

        return true;
    }

    @Transactional
    public boolean updatePost(Long postId, String title, String detail, Integer basicPrice) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다. postId: " + postId));

        post.updatePost(title, detail);
        post.updateBasicPrice(basicPrice);

        return true;
    }


    /**
     * Post -> BasicOption -> AdditionalOption 수정하는 메서드
     * @param postId
     * @param additionalOptionCreate
     * @param prevAdditionalOptionId
     * @return
     */
    @Transactional
    public boolean updateAdditionalOption(Long postId, AdditionalOptionCreate additionalOptionCreate, Long prevAdditionalOptionId) {
        // 1. 기존의 AdditionalOption 삭제
        this.removeAdditionalOption(postId, prevAdditionalOptionId);

        // 2. 새로운 AdditionalOption 추가
        this.addAdditionalOption(postId, additionalOptionCreate);

        return true;
    }


    /**
     * Post -> BasicOption -> AdditionalOption 삭제하는 메서드. 실제로 삭제하지는 않고 deactivate함.
     * @param postId
     * @param additionalOptionId
     * @return
     */
    @Transactional
    public boolean removeAdditionalOption(Long postId, Long additionalOptionId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다. postId: " + postId));

        return post.getBasicOption().removeAdditionalOption(additionalOptionId);
    }

    /**
     * Post의 모든 정보를 조회
     * @param postId 조회할 포스트 ID
     * @return 조회된 포스트 엔티티
     */
    public Post findPostByIdWithDetails(Long postId) {
        return postRepository.findPostWithDetailsById(postId)
                .orElseThrow();
    }



}
