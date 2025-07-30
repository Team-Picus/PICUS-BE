package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.request.UpdatePostWebReq;
import com.picus.core.post.adapter.in.web.mapper.UpdatePostWebMapper;
import com.picus.core.post.application.port.in.UpdatePostUseCase;
import com.picus.core.post.application.port.in.request.UpdatePostCommand;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class UpdatePostController {

    private final UpdatePostUseCase updatePostUseCase;
    private final UpdatePostWebMapper updatePostWebMapper;

    @PatchMapping("/{post_no}")
    public BaseResponse<Void> updatePost(
            @RequestBody UpdatePostWebReq webReq, @PathVariable("post_no") String postNo, @CurrentUser String userNo) {
        UpdatePostCommand appReq = updatePostWebMapper.toAppReq(webReq, postNo, userNo);
        updatePostUseCase.update(appReq);
        return BaseResponse.onSuccess();
    }
}
