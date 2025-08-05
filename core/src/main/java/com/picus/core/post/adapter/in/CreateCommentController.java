package com.picus.core.post.adapter.in;


import com.picus.core.post.adapter.in.web.data.request.CreateCommentRequest;
import com.picus.core.post.adapter.in.web.mapper.CreateCommentWebMapper;
import com.picus.core.post.application.port.in.CreateCommentUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class CreateCommentController {

    private final CreateCommentUseCase createCommentUseCase;
    private final CreateCommentWebMapper webMapper;

    @PostMapping("/{post_no}/comments")
    public BaseResponse<Void> create(
            @PathVariable("post_no") String postNo, @RequestBody CreateCommentRequest request,
            @CurrentUser String userNo) {
        createCommentUseCase.create(webMapper.toCommand(postNo, userNo, request));
        return BaseResponse.onSuccess();
    }
}
