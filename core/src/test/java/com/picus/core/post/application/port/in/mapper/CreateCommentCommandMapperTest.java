package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.command.CreateCommentCommand;
import com.picus.core.post.domain.Comment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CreateCommentCommandMapperTest {

    private final CreateCommentCommandMapper commandMapper = new CreateCommentCommandMapper();
    @Test
    @DisplayName("CreateCommentCommand -> Comment 매핑")
    public void toDomain() throws Exception {
        // given
        CreateCommentCommand command = CreateCommentCommand.builder()
                .postNo("post-123")
                .authorNo("user-123")
                .content("contents")
                .build();

        // when
        Comment domain = commandMapper.toDomain(command);

        // then
        assertThat(domain.getPostNo()).isEqualTo(command.postNo());
        assertThat(domain.getAuthorNo()).isEqualTo(command.authorNo());
        assertThat(domain.getContent()).isEqualTo(command.content());
    }


}