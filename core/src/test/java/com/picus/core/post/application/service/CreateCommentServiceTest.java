package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.command.CreateCommentCommand;
import com.picus.core.post.application.port.in.mapper.CreateCommentCommandMapper;
import com.picus.core.post.application.port.out.CommentCreatePort;
import com.picus.core.post.domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCommentServiceTest {

    @Mock
    CommentCreatePort commentCreatePort;
    @Mock
    CreateCommentCommandMapper commandMapper;

    @InjectMocks
    CreateCommentService service;

    @Test
    @DisplayName("댓글 저장")
    public void create() throws Exception {
        // given
        CreateCommentCommand mockCommand = mock(CreateCommentCommand.class);
        Comment mockComment = mock(Comment.class);
        given(commandMapper.toDomain(mockCommand)).willReturn(mockComment);

        // when
        service.create(mockCommand);

        // then
        then(commandMapper).should().toDomain(mockCommand);
        then(commentCreatePort).should().save(mockComment);
    }

}