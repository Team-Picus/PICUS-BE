package com.picus.core.post.application.service;

import com.picus.core.post.application.port.out.CommentDeletePort;
import com.picus.core.post.application.port.out.CommentReadPort;
import com.picus.core.post.domain.Comment;
import com.picus.core.shared.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DeleteCommentServiceTest {

    @Mock
    private CommentReadPort commentReadPort;
    @Mock
    private CommentDeletePort commentDeletePort;

    @InjectMocks
    private DeleteCommentService service;

    @Test
    @DisplayName("댓글을 삭제한다.")
    public void delete() throws Exception {
        // given
        String commentNo = "cmt-123";
        String currentUserNo = "user-123";

        Comment mockComment = Comment.builder()
                .authorNo(currentUserNo)
                .build();
        given(commentReadPort.findById(commentNo)).willReturn(Optional.ofNullable(mockComment));

        // when
        service.delete(commentNo, currentUserNo);

        // then
        then(commentDeletePort).should().delete(commentNo);
        then(commentDeletePort).should().delete(commentNo);
    }

    @Test
    @DisplayName("댓글을 삭제할 때, 삭제하려는 댓글의 작성자와 현재 사용자가 다르면 예외가 발생한다.")
    public void delete_error() throws Exception {
        // given
        String commentNo = "cmt-123";
        String currentUserNo = "user-123";

        Comment mockComment = Comment.builder()
                .authorNo("user-345")
                .build();
        given(commentReadPort.findById(commentNo)).willReturn(Optional.ofNullable(mockComment));

        // when // then
        assertThatThrownBy(() -> service.delete(commentNo, currentUserNo))
                .isInstanceOf(RestApiException.class);
    }

}