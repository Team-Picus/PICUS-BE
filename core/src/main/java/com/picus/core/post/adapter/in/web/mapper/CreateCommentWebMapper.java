package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.request.CreateCommentRequest;
import com.picus.core.post.application.port.in.command.CreateCommentCommand;
import org.springframework.stereotype.Component;

@Component
public class CreateCommentWebMapper {
    public CreateCommentCommand toCommand(String postNo, String authorNo, CreateCommentRequest request) {
        return CreateCommentCommand.builder()
                .postNo(postNo)
                .authorNo(authorNo)
                .content(request.content())
                .build();
    }
}
