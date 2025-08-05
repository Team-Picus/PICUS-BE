package com.picus.core.post.adapter.out.persistence;

import com.picus.core.post.adapter.out.persistence.entity.CommentEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.mapper.CommentPersistenceMapper;
import com.picus.core.post.adapter.out.persistence.repository.CommentJpaRepository;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.application.port.out.CommentCreatePort;
import com.picus.core.post.application.port.out.CommentDeletePort;
import com.picus.core.post.application.port.out.CommentReadPort;
import com.picus.core.post.domain.Comment;
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@PersistenceAdapter
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements CommentCreatePort, CommentReadPort, CommentDeletePort {

    private final CommentJpaRepository commentJpaRepository;
    private final PostJpaRepository postJpaRepository;

    private final CommentPersistenceMapper persistenceMapper;

    @Override
    public Comment save(Comment comment) {
        // CommentEntity로 매핑
        CommentEntity commentEntity = persistenceMapper.toEntity(comment);

        // CommentEntity의 PostEntity 조회 및 바인딩
        PostEntity postEntity = postJpaRepository.findById(comment.getPostNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        commentEntity.bindPostEntity(postEntity);

        // 저장
        CommentEntity saved = commentJpaRepository.save(commentEntity);

        return persistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Comment> findById(String commentNo) {
        return Optional.empty();
    }

    @Override
    public void delete(String commentNo) {

    }
}
