package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.CreateCommentUseCase;
import com.picus.core.post.application.port.in.command.CreateCommentCommand;
import com.picus.core.post.application.port.in.mapper.CreateCommentCommandMapper;
import com.picus.core.post.application.port.out.CommentCreatePort;
import com.picus.core.post.domain.Comment;
import com.picus.core.shared.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class CreateCommentService implements CreateCommentUseCase {

    private final CommentCreatePort commentCreatePort;

    private final CreateCommentCommandMapper commandMapper;

    @Override
    public void create(CreateCommentCommand command) {
        // Comment 도메인 객체 생성
        Comment comment = commandMapper.toDomain(command);

        // Comment 저장
        commentCreatePort.save(comment);
    }
}
