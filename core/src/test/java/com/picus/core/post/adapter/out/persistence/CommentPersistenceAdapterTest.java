package com.picus.core.post.adapter.out.persistence;

import com.picus.core.post.adapter.out.persistence.entity.CommentEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.mapper.CommentPersistenceMapper;
import com.picus.core.post.adapter.out.persistence.repository.CommentJpaRepository;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.domain.Comment;
import com.picus.core.post.domain.vo.SpaceType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.picus.core.post.domain.vo.PostMoodType.VINTAGE;
import static com.picus.core.post.domain.vo.PostThemeType.BEAUTY;
import static org.assertj.core.api.Assertions.assertThat;

@Import({
        CommentPersistenceAdapter.class,
        CommentPersistenceMapper.class
})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentPersistenceAdapterTest {


    @Autowired
    private PostJpaRepository postJpaRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private CommentPersistenceAdapter commentPersistenceAdapter;
    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @Test
    @DisplayName("CommentEntity를 저장한다.")
    public void save() throws Exception {
        // given - 데이터베이스 데이터 셋팅
        PostEntity postEntity = createPostEntity();
        clearPersistenceContext();

        // given - 저장할 도메인 객체 셋팅
        Comment comment = createCommentDomain(postEntity);

        // when - 저장
        Comment saved = commentPersistenceAdapter.save(comment);
        clearPersistenceContext();

        // then
        Optional<CommentEntity> optionalCommentEntity = commentJpaRepository.findById(saved.getCommentNo());

        assertThat(optionalCommentEntity).isPresent();
        CommentEntity commentEntity = optionalCommentEntity.get();
        assertThat(commentEntity.getCommentNo()).isEqualTo(saved.getCommentNo());
        assertThat(commentEntity.getUserNo()).isEqualTo(saved.getAuthorNo());
        assertThat(commentEntity.getContent()).isEqualTo(saved.getContent());
    }

    @Test
    @DisplayName("commentNo로 Comment를 조회한다.")
    public void findById() throws Exception {
        // given - 데이터베이스 데이터 셋팅
        PostEntity postEntity = createPostEntity();
        CommentEntity commentEntity = createCommentEntity(postEntity, "user-123", "content");
        clearPersistenceContext();

        // when
        Optional<Comment> optionalComment = commentPersistenceAdapter.findById(commentEntity.getCommentNo());

        // then
        assertThat(optionalComment).isPresent();

        Comment comment = optionalComment.get();
        assertThat(comment.getCommentNo()).isEqualTo(commentEntity.getCommentNo());
        assertThat(comment.getPostNo()).isEqualTo(postEntity.getPostNo());
        assertThat(comment.getAuthorNo()).isEqualTo(commentEntity.getUserNo());
        assertThat(comment.getContent()).isEqualTo(commentEntity.getContent());
    }
    
    @Test
    @DisplayName("특정 commentNo의 Comment를 삭제한다.")
    public void delete() throws Exception {
        // given - 데이터베이스 데이터 셋팅
        PostEntity postEntity = createPostEntity();
        CommentEntity commentEntity = createCommentEntity(postEntity);
        clearPersistenceContext();
        
        // when
        commentPersistenceAdapter.delete(commentEntity.getCommentNo());
        
        // then
        assertThat(commentJpaRepository.findById(commentEntity.getCommentNo()))
                .isNotPresent();
    }

    private PostEntity createPostEntity() {
        PostEntity postEntity = PostEntity.builder()
                .packageNo("packageNo")
                .expertNo("expertNo")
                .title("title")
                .oneLineDescription("oneLineDescription")
                .detailedDescription("detailedDescription")
                .postThemeTypes(List.of(BEAUTY))
                .snapSubThemes(List.of())
                .postMoodTypes(List.of(VINTAGE))
                .spaceType(SpaceType.OUTDOOR)
                .spaceAddress("spaceAddress")
                .isPinned(false)
                .build();
        return postJpaRepository.save(postEntity);
    }

    private CommentEntity createCommentEntity(PostEntity postEntity, String userNo, String content) {
        CommentEntity commentEntity = CommentEntity.builder()
                .postEntity(postEntity)
                .userNo(userNo)
                .content(content)
                .build();
        return commentJpaRepository.save(commentEntity);
    }
    private CommentEntity createCommentEntity(PostEntity postEntity) {
        CommentEntity commentEntity = CommentEntity.builder()
                .postEntity(postEntity)
                .userNo("userNo")
                .content("content")
                .build();
        return commentJpaRepository.save(commentEntity);
    }

    private Comment createCommentDomain(PostEntity postEntity) {
        return Comment.builder()
                .postNo(postEntity.getPostNo())
                .authorNo("user-123")
                .content("content")
                .build();
    }


    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }
}