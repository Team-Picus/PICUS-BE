package com.picus.core.post.adapter.in;

import com.picus.core.post.adapter.in.web.data.request.UpdatePostWebReq;
import com.picus.core.post.adapter.in.web.data.request.WritePostWebReq;
import com.picus.core.post.adapter.in.web.mapper.UpdatePostWebMapper;
import com.picus.core.post.adapter.in.web.mapper.WritePostWebMapper;
import com.picus.core.post.application.port.in.PostInfoCommand;
import com.picus.core.post.application.port.in.request.UpdatePostAppReq;
import com.picus.core.post.application.port.in.request.WritePostAppReq;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostInfoCommandController {

    private final PostInfoCommand postInfoCommand;

    private final WritePostWebMapper writePostWebMapper;
    private final UpdatePostWebMapper updatePostWebMapper;

    @PostMapping("/api/v1/posts")
    public BaseResponse<Void> writePost(@RequestBody @Valid WritePostWebReq webReq, @CurrentUser String userNo) {
        WritePostAppReq appReq = writePostWebMapper.toAppReq(webReq, userNo);
        postInfoCommand.write(appReq);
        return BaseResponse.onSuccess();
    }

    @PatchMapping("/api/v1/posts/{post_no}")
    public BaseResponse<Void> updatePost(
            @RequestBody UpdatePostWebReq webReq, @PathVariable("post_no") String postNo, @CurrentUser String userNo) {
        UpdatePostAppReq appReq = updatePostWebMapper.toAppReq(webReq, postNo, userNo);
        postInfoCommand.update(appReq);
        return BaseResponse.onSuccess();
    }
}
