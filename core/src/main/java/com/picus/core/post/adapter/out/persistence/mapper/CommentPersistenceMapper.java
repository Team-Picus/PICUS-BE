package com.picus.core.post.adapter.out.persistence.mapper;

import com.picus.core.post.adapter.out.persistence.entity.CommentEntity;
import com.picus.core.post.domain.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentPersistenceMapper {

    public CommentEntity toEntity(Comment domain) {
        return CommentEntity.builder()
                .userNo(domain.getAuthorNo())
                .content(domain.getContent())
                .build();
    }

    public Comment toDomain(CommentEntity entity) {
        return Comment.builder()
                .commentNo(entity.getCommentNo())
                .postNo(entity.getPostEntity().getPostNo())
                .authorNo(entity.getUserNo())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}
