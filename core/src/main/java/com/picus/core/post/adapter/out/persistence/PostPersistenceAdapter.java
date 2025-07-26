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
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;


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
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));




    }

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
