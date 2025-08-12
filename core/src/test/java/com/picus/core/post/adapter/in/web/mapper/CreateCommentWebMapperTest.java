package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.request.CreateCommentRequest;
import com.picus.core.post.application.port.in.command.CreateCommentCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateCommentWebMapperTest {

    private final CreateCommentWebMapper webMapper = new CreateCommentWebMapper();

    @Test
    @DisplayName("postNo, currentUserNo, CreateCommentRequest -> CreateCommentCommand")
    public void toCommand() throws Exception {
        // given
        String postNo = "post-123";
        String authorNo = "user-123";
        CreateCommentRequest request = CreateCommentRequest.builder().content("content").build();

        // when
        CreateCommentCommand command = webMapper.toCommand(postNo, authorNo, request);

        // then
        assertThat(command.postNo()).isEqualTo(postNo);
        assertThat(command.authorNo()).isEqualTo(authorNo);
        assertThat(command.content()).isEqualTo(request.content());
    }

}