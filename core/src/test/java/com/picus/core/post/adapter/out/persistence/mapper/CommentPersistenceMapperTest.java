package com.picus.core.post.adapter.out.persistence.mapper;

import com.picus.core.post.adapter.out.persistence.entity.CommentEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentPersistenceMapperTest {

    private final CommentPersistenceMapper mapper = new CommentPersistenceMapper();

    @Test
    @DisplayName("Comment -> CommentEntity 매핑")
    public void toEntity() throws Exception {
        // given
        Comment comment = Comment.builder()
                .authorNo("user-123")
                .content("content")
                .build();

        // when
        CommentEntity entity = mapper.toEntity(comment);

        // then
        assertThat(entity.getUserNo()).isEqualTo(comment.getAuthorNo());
        assertThat(entity.getContent()).isEqualTo(comment.getContent());
    }

    @Test
    @DisplayName("CommentEntity -> Comment 매핑")
    public void toDomain() throws Exception {
        // given
        LocalDateTime baseTime = LocalDateTime.of(2020, 10, 10, 10, 10);
        CommentEntity entity = CommentEntity.builder()
                .commentNo("cmt-123")
                .postEntity(PostEntity.builder()
                        .postNo("post-123")
                        .build())
                .userNo("user-123")
                .content("content")
                .createdAt(baseTime.minusDays(2))
                .updatedAt(baseTime.minusDays(1))
                .deletedAt(baseTime)
                .build();

        // when
        Comment domain = mapper.toDomain(entity);

        // then
        assertThat(domain.getCommentNo()).isEqualTo(entity.getCommentNo());
        assertThat(domain.getPostNo()).isEqualTo(entity.getPostEntity().getPostNo());
        assertThat(domain.getAuthorNo()).isEqualTo(entity.getUserNo());
        assertThat(domain.getContent()).isEqualTo(entity.getContent());
        assertThat(domain.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(domain.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
        assertThat(domain.getDeletedAt()).isEqualTo(entity.getDeletedAt());
    }
}