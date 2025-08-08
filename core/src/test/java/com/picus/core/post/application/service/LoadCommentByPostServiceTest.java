package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.result.LoadCommentByPostResult;
import com.picus.core.post.application.port.out.CommentReadPort;
import com.picus.core.post.domain.Comment;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class LoadCommentByPostServiceTest {

    @Mock
    private CommentReadPort commentReadPort;
    @Mock
    private UserReadPort userReadPort;

    @InjectMocks
    private LoadCommentByPostService service;

    @Test
    @DisplayName("특정 게시물의 댓글들을 조회한다.")
    public void load() throws Exception {
        // given
        String postNo = "post-123";

        Comment mockComment = Comment.builder()
                .commentNo("cmt-123")
                .authorNo("user-123")
                .content("content")
                .createdAt(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();
        given(commentReadPort.findByPostNo(postNo)).willReturn(List.of(mockComment));

        User mockUser = User.builder()
                .nickname("nick")
                .build();
        given(userReadPort.findById(mockComment.getAuthorNo())).willReturn(mockUser);


        // when
        List<LoadCommentByPostResult> results = service.load(postNo);

        // then
        then(commentReadPort).should().findByPostNo(postNo);
        then(userReadPort).should().findById(mockComment.getAuthorNo());

        assertThat(results).hasSize(1)
                .extracting(
                        LoadCommentByPostResult::commentNo,
                        LoadCommentByPostResult::authorNo,
                        LoadCommentByPostResult::authorNickname,
                        LoadCommentByPostResult::authorProfileImageUrl,
                        LoadCommentByPostResult::content,
                        LoadCommentByPostResult::createdAt
                ).containsExactlyInAnyOrder(
                        tuple(
                                mockComment.getCommentNo(),
                                mockComment.getAuthorNo(),
                                mockUser.getNickname(),
                                "",
                                mockComment.getContent(),
                                mockComment.getCreatedAt()
                        )
                );
    }

}