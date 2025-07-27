package com.picus.core.post.adapter.out.persistence;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import com.picus.core.post.adapter.out.persistence.mapper.PostImagePersistenceMapper;
import com.picus.core.post.adapter.out.persistence.mapper.PostPersistenceMapper;
import com.picus.core.post.adapter.out.persistence.repository.PostImageJpaRepository;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.application.port.out.PostCommandPort;
import com.picus.core.post.application.port.out.PostQueryPort;
import com.picus.core.post.domain.model.Post;
import com.picus.core.post.domain.model.PostImage;
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;


@PersistenceAdapter
@RequiredArgsConstructor
public class PostPersistenceAdapter implements PostCommandPort, PostQueryPort {

    private final PostJpaRepository postJpaRepository;
    private final PostImageJpaRepository postImageJpaRepository;

    private final PostPersistenceMapper postPersistenceMapper;
    private final PostImagePersistenceMapper postImagePersistenceMapper;

    @Override
    public Post save(Post post) {
        PostEntity postEntity = savePostEntity(post); // PostEntity 저장

        List<PostImage> postImages = savePostImageEntities(post.getPostImages(), postEntity); // 해당 Post의 PostImageEntity 저장

        return postPersistenceMapper.toDomain(postEntity, postImages);
    }

    @Override
    public Optional<Post> findById(String postNo) {
        Optional<PostEntity> optionalPostEntity = postJpaRepository.findById(postNo);

        if (optionalPostEntity.isEmpty()) {
            return Optional.empty();
        }

        PostEntity postEntity = optionalPostEntity.get();
        List<PostImageEntity> imageEntities = postImageJpaRepository.findByPostEntity_PostNo(postNo);
        List<PostImage> postImages = imageEntities.stream()
                .map(postImagePersistenceMapper::toDomain)
                .toList();

        Post post = postPersistenceMapper.toDomain(postEntity, postImages);
        return Optional.of(post);
    }

    @Override
    public void update(Post post, List<String> deletedPostImageNos) {
        PostEntity postEntity = postJpaRepository.findById(post.getPostNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // PostEntity 수정
        postEntity.updatePostEntity(
                post.getPackageNo(), post.getTitle(), post.getOneLineDescription(),
                post.getDetailedDescription(), post.getPostThemeTypes(), post.getPostMoodTypes(),
                post.getSpaceType(), post.getSpaceAddress(), post.getIsPinned()
        );

        // PostImageEntity 수정
        // 삭제
        postImageJpaRepository.deleteByPostImageNoIn(deletedPostImageNos);

        // 삭제 후 이미지 순서 재정렬
        List<PostImageEntity> imagesAfterDelete = postImageJpaRepository.findByPostEntity_PostNoOrderByImageOrder(post.getPostNo());
        for (int i = 0; i < imagesAfterDelete.size(); i++) {
            imagesAfterDelete.get(i).assignImageOrder(i + 1);
        }

        // 추가 수정 전에 모든 imageOrder 값을 임시값으로 변경
        postImageJpaRepository.shiftAllImageOrdersToNegative(post.getPostNo());

        // 추가/수정
        List<PostImage> postImages = post.getPostImages();
        for (PostImage domain : postImages) {
            String postImageNo = domain.getPostImageNo();
            if(postImageNo != null) {
                // pk가 있다 = 수정
                PostImageEntity entity = postImageJpaRepository.findById(postImageNo)
                        .orElseThrow(() -> new RestApiException(_NOT_FOUND));
                entity.updatePostImageEntity(domain.getFileKey(), domain.getImageOrder());
            } else {
                // pk가 없다 = 추가
                PostImageEntity entity = postImagePersistenceMapper.toEntity(domain);
                entity.bindPostEntity(postEntity);
                postImageJpaRepository.save(entity);
            }
        }

        // 최종적으로 재정렬
        List<PostImageEntity> finalImages = postImageJpaRepository.findByPostEntity_PostNoOrderByImageOrder(post.getPostNo());

        for (int i = 0; i < finalImages.size(); i++) {
            finalImages.get(i).assignImageOrder(i + 1);
        }
    }

    @Override
    public void delete(String postNo) {
        // PostImageEntity 삭제
        postImageJpaRepository.deleteByPostEntity_PostNo(postNo);

        // PostEntity 삭제
        postJpaRepository.deleteById(postNo);
    }

    /**
     * private 메서드
     */
    private PostEntity savePostEntity(Post post) {
        PostEntity postEntity = postPersistenceMapper.toEntity(post);
        postJpaRepository.save(postEntity);
        return postEntity;
    }

    private List<PostImage> savePostImageEntities(List<PostImage> postImages, PostEntity postEntity) {
        List<PostImageEntity> postImageEntities = postImages.stream()
                .map(postImage -> {
                    PostImageEntity entity = postImagePersistenceMapper.toEntity(postImage);
                    entity.bindPostEntity(postEntity);
                    return entity;
                })
                .toList();

        return postImageJpaRepository.saveAll(postImageEntities).stream()
                .map(postImagePersistenceMapper::toDomain)
                .toList();
    }
}
