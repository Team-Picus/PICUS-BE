package com.picus.core.domain.post.domain.service;

import com.picus.core.domain.post.application.dto.request.AdditionalOptionDto;
import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.domain.post.domain.repository.PostRepository;
import com.picus.core.global.common.area.entity.District;
import com.picus.core.global.common.category.entity.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * @param studioNo 스튜디오 id
     * @return 생성된 포스트 엔티티
     */
    @Transactional
    public Post create(Long studioNo) {
        return new Post(studioNo);
    }
    /**
     * 포스트 내용을 저장하는 메서드
     *
     * @param postId           포스트 ID
     * @param title            포스트 제목
     * @param detail           포스트 상세 내용
     * @param basicPrice       기본 옵션 가격
     * @return 저장된 Post 엔티티
     */
    @Transactional
    public Post initailPost(Long postId,
                         String title,
                         String detail,
                         Integer basicPrice) {
        // 1. Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스튜디오를 찾을 수 없습니다. studioNo: " + postId));

        // 2. Post 초기화 (DRAFT 상태에서 title, detail, availableAreas, 기본 옵션 설정)
        post.initialize(title, detail,  basicPrice);

        // 5. Repository를 통해 Post 저장 (cascade 옵션 덕분에 연관된 엔티티들도 함께 persist)
        return postRepository.save(post);
    }

    public void addAdditionalOption(Long postId, AdditionalOptionDto additionalOptionDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다. postId: " + postId));

        post.getBasicOption().addAdditionalOption(
                additionalOptionDto.name(),
                additionalOptionDto.pricePerUnit(),
                additionalOptionDto.max(),
                additionalOptionDto.base(),
                additionalOptionDto.increment()
        );
    }

//    /**
//     * 포스트에 이미지를 추가하는 메서드
//     * @param postId 포스트 ID
//     * @param imageKey 추가할 이미지 키
//     * @return
//     */
//    @Transactional
//    public void addImages(Long postId, String imageKey) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다. postId: " + postId));
//
//        post.addPostImageResource(imageKey);
//    }

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
}
