package com.picus.core.post.adapter.in;

import com.picus.core.post.application.port.in.DeletePostUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class DeleteCommentController {

    private final DeletePostUseCase deletePostUseCase;

    @DeleteMapping("/{comment_no}")
    public BaseResponse<Void> delete(@PathVariable("comment_no") String commentNo, @CurrentUser String userNo) {
        deletePostUseCase.delete(commentNo, userNo);
        return BaseResponse.onSuccess();
    }
}
