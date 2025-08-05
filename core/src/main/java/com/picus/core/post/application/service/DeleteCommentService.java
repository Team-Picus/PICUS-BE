package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.DeleteCommentUseCase;
import com.picus.core.post.application.port.out.CommentDeletePort;
import com.picus.core.post.application.port.out.CommentReadPort;
import com.picus.core.post.domain.Comment;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._FORBIDDEN;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional
public class DeleteCommentService implements DeleteCommentUseCase {

    private final CommentReadPort commentReadPort;
    private final CommentDeletePort commentDeletePort;

    @Override
    public void delete(String commentNo, String currentUserNo) {
        // 댓글 조회
        Comment comment = commentReadPort.findById(commentNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // 삭제하려는 댓글의 작성자와 현재 사용자가 일치하는지 검증
//        throwIfNotOwner(comment.getAuthorNo(), currentUserNo);

        // 삭제
        commentDeletePort.delete(commentNo);
    }

    private void throwIfNotOwner(String commentAuthorNo, String currentUserNo) {
        if (!commentAuthorNo.equals(currentUserNo))
            throw new RestApiException(_FORBIDDEN);
    }
}
