package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.command.CreateCommentCommand;
import com.picus.core.post.domain.Comment;
import org.springframework.stereotype.Component;

@Component
public class CreateCommentCommandMapper {

    public Comment toDomain(CreateCommentCommand command) {
        return Comment.builder()
                .postNo(command.postNo())
                .authorNo(command.authorNo())
                .content(command.content())
                .build();
    }
}


